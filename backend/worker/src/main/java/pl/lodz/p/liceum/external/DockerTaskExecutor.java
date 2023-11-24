package pl.lodz.p.liceum.external;

import lombok.extern.java.Log;

import org.springframework.stereotype.Service;
import pl.lodz.p.liceum.domain.ExecutionStatus;
import pl.lodz.p.liceum.domain.Task;
import pl.lodz.p.liceum.domain.TaskExecutor;

import java.io.IOException;

import static java.lang.Runtime.getRuntime;
import static pl.lodz.p.liceum.domain.ExecutionStatus.COMPLETED;
import static pl.lodz.p.liceum.domain.ExecutionStatus.FAILED;

@Log
@Service
public class DockerTaskExecutor implements TaskExecutor {

    private static final int SUCCESS = 0;

    @Override
    public ExecutionStatus execute(Task task) {
        try {
            log.info("Task started");
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
        return new String[] { "bash", "-c", "cd " + workspaceUrl + ";docker-compose up" };
    }

}
