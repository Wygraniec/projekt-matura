package pl.lodz.p.liceum.matura.api.user;

import java.time.ZonedDateTime;

public record UserDto(
        Integer id,
        String email,
        String username,
        String password,
        String role,
        Integer createdBy,
        ZonedDateTime createdAt
) {
}
