package pl.lodz.p.liceum.matura.api.user;

import java.time.ZonedDateTime;

public record UserDto(
        Integer id,
        String email,
        String name,
        String password,
        String role,
        ZonedDateTime createdAt
) {}
