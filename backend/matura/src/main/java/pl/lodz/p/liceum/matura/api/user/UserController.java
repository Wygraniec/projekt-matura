package pl.lodz.p.liceum.matura.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.liceum.matura.api.appservices.UserApplicationService;
import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.domain.user.UserService;
import pl.lodz.p.liceum.matura.security.Security;


@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/users")
class UserController {

    private final UserApplicationService userService;
    private final UserDtoMapper userMapper;
    private final PageUserDtoMapper pageUserDtoMapper;
    private final Security security;

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer id) {
        User user = userService.findById(id);
        return ResponseEntity
                .ok(userMapper.toDto(user));
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


}
