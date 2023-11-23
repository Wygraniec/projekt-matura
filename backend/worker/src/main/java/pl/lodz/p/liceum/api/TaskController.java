package pl.lodz.p.liceum.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.liceum.domain.Task;
import pl.lodz.p.liceum.domain.TaskExecutor;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/tasks")
class TaskController {

    private final TaskExecutor taskExecutor;

    @PostMapping
    public ResponseEntity<Void> executeTask(@RequestBody ExecuteCommand command) {
        taskExecutor.execute(new Task(command.workspaceUrl()));
        return ResponseEntity.ok().build();
    }

}
