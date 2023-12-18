package pl.lodz.p.liceum.matura.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.matura.domain.ExecutionStatus;
import pl.lodz.p.liceum.matura.domain.Subtask;
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

    private ExecutionStatus execute(Task task) {
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

    @Override
    public ExecutionStatus executeTask(Task task) {
        log.info("Task started");

        TaskDefinition taskDefinition = taskDefinitionParser.parse(task.getWorkspaceUrl() + "/task_definition.yml");

        dockerComposeGenerator.generate(
                task.getWorkspaceUrl() + "/docker-compose.yml",
                taskDefinition.getEnvironment(),
                taskDefinition.getVerification()
        );

        return execute(task);
    }

    @Override
    public ExecutionStatus executeSubtask(Subtask subtask) {
        log.info("Subtask started");

        TaskDefinition taskDefinition = taskDefinitionParser.parse(subtask.getWorkspaceUrl() + "/task_definition.yml");

        CheckData checkData = taskDefinition
                .getTasks()
                .get(subtask.getName())
                .getCheckTypes()
                .get(subtask.getType().toString());

        dockerComposeGenerator.generate(
                subtask.getWorkspaceUrl() + "/docker-compose.yml",
                taskDefinition.getEnvironment(),
                checkData
        );

        return execute(subtask);
    }

    private String[] prepareCommand(String workspaceUrl) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows)
            return new String[]{"cmd.exe", "/c", "docker-compose --file " + workspaceUrl + "\\docker-compose.yml up"};
        else
            return new String[]{"bash", "-c", "cd " + workspaceUrl + ";docker-compose up"};
    }

}

