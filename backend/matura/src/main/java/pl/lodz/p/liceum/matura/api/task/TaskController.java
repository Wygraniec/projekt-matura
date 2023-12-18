package pl.lodz.p.liceum.matura.api.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.liceum.matura.domain.task.Subtask;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.task.TaskExecutor;
import pl.lodz.p.liceum.matura.domain.task.TestType;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/tasks")
class TaskController {

    private final TaskExecutor taskExecutor;
    @PostMapping(path = "{taskId}/subtasks/{subtaskId}/fastprocess")
    public ResponseEntity<Void> executeSubtaskFastProcessing(
            @PathVariable String taskId,
            @PathVariable String subtaskId,
            @RequestBody ExecuteCommand command) {
        taskExecutor.executeSubtask(new Subtask(command.workspaceUrl(), subtaskId, TestType.FAST));
        return ResponseEntity.ok().build();
    }
    @PostMapping(path = "{taskId}/subtasks/{subtaskId}/fullprocess")
    public ResponseEntity<Void> executeSubtaskFullProcessing(
            @PathVariable String taskId,
            @PathVariable String subtaskId,
            @RequestBody ExecuteCommand command) {
        taskExecutor.executeSubtask(new Subtask(command.workspaceUrl(), subtaskId, TestType.FULL));
        return ResponseEntity.ok().build();
    }
    @PostMapping("{taskId}/process")
    public ResponseEntity<Void> executeTask(
       @PathVariable String taskId,
       @RequestBody ExecuteCommand command
    ) {
        taskExecutor.executeTask(new Task(command.workspaceUrl()));
        return ResponseEntity.ok().build();
    }
}
