package pl.lodz.p.liceum.matura.api.user;

import java.time.ZonedDateTime;

public record UserDto(
        Integer id,
        String email,
        String nickname,
        String password,
        String role,
        ZonedDateTime createdAt
) {}
