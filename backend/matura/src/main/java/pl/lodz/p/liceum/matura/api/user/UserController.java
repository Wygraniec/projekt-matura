package pl.lodz.p.liceum.matura.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.liceum.matura.api.task.TaskDto;
import pl.lodz.p.liceum.matura.api.task.TaskDtoMapper;
import pl.lodz.p.liceum.matura.appservices.TaskApplicationService;
import pl.lodz.p.liceum.matura.appservices.UserApplicationService;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.security.Security;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/users")
class UserController {

    private final UserApplicationService userService;
    private final TaskApplicationService taskService;
    private final UserDtoMapper userMapper;
    private final TaskDtoMapper taskMapper;
    private final PageUserDtoMapper pageUserDtoMapper;
    private final Security security;

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        User user = userService.findById(id);
        return ResponseEntity
                .ok(userMapper.toDto(user));
    }

    @GetMapping(path = "/{id}/tasks")
    public ResponseEntity<List<Integer>> getUserTaskIds(@PathVariable Integer id) {
        return ResponseEntity.ok(
                taskService.findByUserId(id)
                        .stream()
                        .map(Task::getId)
                        .toList()
        );
    }

    @GetMapping
    public ResponseEntity<PageUserDto> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageUserDto pageUsers = pageUserDtoMapper.toPageDto(userService.findAll(pageable));

        return ResponseEntity.ok(pageUsers);
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto dto) {
        User user = userService.save(userMapper.toDomain(dto));
        return ResponseEntity
                .ok(userMapper.toDto(user));
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody UserDto dto) {
        userService.update(userMapper.toDomain(dto));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeUser(@PathVariable Integer id) {
        userService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("me")
    public ResponseEntity<UserDto> aboutMe() {
        User user = security.getPrincipal();

        return ResponseEntity
                .ok(userMapper.toDto(user));
    }
    @GetMapping(path = "/{id}/pendingTask")
    public ResponseEntity<TaskDto> getUser(
            @PathVariable Integer id,
            @RequestParam Integer templateId
    ) {
        Optional<Task> task = taskService.findPendingTaskForUser(id, templateId);
        return task.map(value -> ResponseEntity.ok(taskMapper.toDto(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
