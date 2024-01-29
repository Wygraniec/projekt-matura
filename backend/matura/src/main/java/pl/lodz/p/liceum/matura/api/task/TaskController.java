package pl.lodz.p.liceum.matura.api.task;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.liceum.matura.api.user.UserDto;
import pl.lodz.p.liceum.matura.appservices.TaskApplicationService;
import pl.lodz.p.liceum.matura.domain.task.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/tasks")
public class TaskController {

    private final TaskApplicationService service;
    private final TaskExecutor taskExecutor;
    private final TaskDtoMapper mapper;

    @GetMapping(path = "/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable Integer id) {
        Task task = service.findById(id);
        return ResponseEntity.ok(mapper.toDto(task));
    }

    @PostMapping
    public ResponseEntity<TaskDto> saveTask(@RequestBody TaskDto dto) {
        return ResponseEntity.ok(
                mapper.toDto(
                        service.save(mapper.toDomain(dto))
                )
        );
    }

    @PutMapping
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto dto) {
        service.update(mapper.toDomain(dto));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Integer id) {
        service.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/createdby/{id}")
    public ResponseEntity<List<TaskDto>> findTasksByCreatedBy(@PathVariable Integer id) {
        return ResponseEntity.ok(
                service.findByCreatedBy(id)
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    @GetMapping("/assignedto/{id}")
    public ResponseEntity<List<TaskDto>> findTasksByUserId(@PathVariable Integer id) {
        return ResponseEntity.ok(
                service.findByUserId(id)
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
    }

    // Task processing
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
            @PathVariable Integer taskId,
            @PathVariable String subtaskId,
            @RequestBody ExecuteCommand command) {
        taskExecutor.executeSubtask(new Subtask(command.workspaceUrl(), subtaskId, TestType.FULL));
        return ResponseEntity.ok().build();
    }

    @PostMapping("{taskId}/process")
    public ResponseEntity<Void> executeTask(
            @PathVariable Integer taskId,
            @RequestBody ExecuteCommand command
    ) {
        taskExecutor.executeTask(service.findById(taskId));
        return ResponseEntity.ok().build();
    }


}
