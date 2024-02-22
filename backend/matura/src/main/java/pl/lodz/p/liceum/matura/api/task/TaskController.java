package pl.lodz.p.liceum.matura.api.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping()
    public ResponseEntity<List<TaskDto>> getTasksByCreatedBy(@RequestParam(name="createdById") Integer createdById) {
        return ResponseEntity.ok(
                service.findByCreatedBy(createdById)
                        .stream()
                        .map(mapper::toDto)
                        .toList()
        );
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
    public ResponseEntity<Void> removeTask(@PathVariable Integer id) {
        service.removeById(id);
        return ResponseEntity.noContent().build();
    }

    
    // Task processing
    @PostMapping(path = "{taskId}/subtasks/{subtaskId}/fastprocess")
    public ResponseEntity<Void> executeSubtaskFastProcessing(
            @PathVariable Integer taskId,
            @PathVariable String subtaskId,
            @RequestBody ExecuteCommand command) {
        Task task = service.findById(taskId);
        taskExecutor.executeSubtask(new Subtask(task.getWorkspaceUrl(), subtaskId, TestType.FAST));
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "{taskId}/subtasks/{subtaskId}/fullprocess")
    public ResponseEntity<Void> executeSubtaskFullProcessing(
            @PathVariable Integer taskId,
            @PathVariable String subtaskId,
            @RequestBody ExecuteCommand command) {
        Task task = service.findById(taskId);
        taskExecutor.executeSubtask(new Subtask(task.getWorkspaceUrl(), subtaskId, TestType.FULL));
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
