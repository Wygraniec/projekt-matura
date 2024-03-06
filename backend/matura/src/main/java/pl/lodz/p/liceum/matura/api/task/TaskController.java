package pl.lodz.p.liceum.matura.api.task;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.lodz.p.liceum.matura.api.response.MessageResponse;
import pl.lodz.p.liceum.matura.appservices.TaskApplicationService;
import pl.lodz.p.liceum.matura.domain.subtask.Subtask;
import pl.lodz.p.liceum.matura.domain.task.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @GetMapping(path = "/{id}/definition")
    public ResponseEntity<Map<String, Object>> getTaskDefinition(@PathVariable Integer id) {
        final Map<String, Object> taskDefinition = service.readTaskDefinitionFile(id);
        return ResponseEntity.ok(taskDefinition);
    }

    @GetMapping(
            path = "/{taskId}/subtasks/{subtaskId}/files/{fileId}"
    )
    public ResponseEntity<Object> getFileAssignedToUserTask(
            @PathVariable Integer taskId,
            @PathVariable Integer subtaskId,
            @PathVariable Integer fileId
    ) {
        return createResponseEntityForFileAssignedToUserTask(taskId, subtaskId, fileId);
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            path = "/{taskId}/subtasks/{subtaskId}/files/{fileId}"
    )
    public ResponseEntity<MessageResponse> postFileAssignedToUserTask(
            @PathVariable Integer taskId,
            @PathVariable Integer subtaskId,
            @PathVariable Integer fileId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Integer fileIndex = fileId - 1;
        service.writeFile(taskId, subtaskId, fileIndex, file.getBytes());

        return ResponseEntity.ok(new MessageResponse("The File Uploaded Successfully"));
    }

    private ResponseEntity<Object> createResponseEntityForFileAssignedToUserTask(Integer taskId, Integer subtaskId, Integer fileId) {
        Integer fileIndex = fileId - 1;
        String fileName = service.getFileName(taskId, subtaskId, fileIndex);
        byte[] file = service.readFile(taskId, subtaskId, fileIndex);
        HttpHeaders headers = prepareHttpHeadersForFileResponse(fileName);
        return ResponseEntity.ok().headers(headers).contentLength(file.length).contentType(MediaType.parseMediaType("application/txt")).body(file);
    }

    private HttpHeaders prepareHttpHeadersForFileResponse (String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return headers;
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
        taskExecutor.executeSubtask(new Subtask(0, task.getId(), subtaskId, TestType.FAST));
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "{taskId}/subtasks/{subtaskId}/fullprocess")
    public ResponseEntity<Void> executeSubtaskFullProcessing(
            @PathVariable Integer taskId,
            @PathVariable String subtaskId,
            @RequestBody ExecuteCommand command) {
        Task task = service.findById(taskId);
        taskExecutor.executeSubtask(new Subtask(0, task.getId(), subtaskId, TestType.FULL));
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
