package pl.lodz.p.liceum.matura.api.auth;

import pl.lodz.p.liceum.matura.api.user.UserDto;

public record AuthenticationResponse(
        String token,
        UserDto userDto
) {
}
