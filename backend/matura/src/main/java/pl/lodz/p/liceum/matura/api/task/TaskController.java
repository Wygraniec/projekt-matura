package pl.lodz.p.liceum.matura.api.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.task.TaskExecutor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/tasks")
class TaskController {

    private final TaskExecutor taskExecutor;

//    @PostMapping
//    public ResponseEntity<Void> executeTask(@RequestBody ExecuteCommand command) {
//        taskExecutor.execute(new Task(command.workspaceUrl()));
//        return ResponseEntity.ok().build();
//    }

    @PostMapping(path = "{taskId}/process")
    public ResponseEntity<Void> executeTask(
            @PathVariable String taskId,
            @RequestBody ExecuteCommand command) {
        taskExecutor.execute(new Task(command.workspaceUrl()));
        return ResponseEntity.ok().build();
    }


//    @PostMapping(path = "{taskId}/subtasks/{subtaskId}/fastprocess")
//    @PostMapping(path = "{taskId}/subtasks/{subtaskId}/fullprocess")
}
