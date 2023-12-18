package pl.lodz.p.liceum.matura.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.matura.domain.ExecutionStatus;
import pl.lodz.p.liceum.matura.domain.Task;
import pl.lodz.p.liceum.matura.domain.TaskExecutor;
import pl.lodz.p.liceum.matura.external.worker.task.DockerComposeGenerator;
import pl.lodz.p.liceum.matura.external.worker.task.TaskDefinitionParser;
import pl.lodz.p.liceum.matura.external.worker.task.definition.CheckData;
import pl.lodz.p.liceum.matura.external.worker.task.definition.TaskDefinition;
import pl.lodz.p.liceum.matura.external.worker.task.definition.TaskEnvironment;

import java.io.*;

import static java.lang.Runtime.getRuntime;
import static pl.lodz.p.liceum.matura.domain.ExecutionStatus.COMPLETED;
import static pl.lodz.p.liceum.matura.domain.ExecutionStatus.FAILED;

@RequiredArgsConstructor
@Log
@Service
public class DockerTaskExecutor implements TaskExecutor {

    private static final int SUCCESS = 0;
    private final TaskDefinitionParser taskDefinitionParser;
    private final DockerComposeGenerator dockerComposeGenerator;

    @Override
    public ExecutionStatus execute(Task task) {
        log.info("Task started");

        TaskDefinition taskDefinition = taskDefinitionParser.parse(task.getWorkspaceUrl() + "/task_definition.yml");

        String dockerComposePath = task.getWorkspaceUrl() + "/docker-compose.yml";
        TaskEnvironment environment = taskDefinition.getEnvironment();
        CheckData checkData = taskDefinition.getTasks().get(task.getName()).getCheckTypes().get(task.getType().toString());

        dockerComposeGenerator.generate(dockerComposePath, environment, checkData);

        try {
            var command = prepareCommand(task.getWorkspaceUrl());
            var process = getRuntime().exec(command);
            log.info("Task finished");
            return SUCCESS == process.waitFor() ? COMPLETED : FAILED;
        } catch (InterruptedException | IOException exception) {
            log.info(exception.toString());
            return FAILED;
        }
    }

    private String[] prepareCommand(String workspaceUrl) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows)
            return new String[]{"cmd.exe", "/c", "docker-compose --file " + workspaceUrl + "\\docker-compose.yml up"};
        else
            return new String[]{"bash", "-c", "cd " + workspaceUrl + ";docker-compose up"};
    }

}

