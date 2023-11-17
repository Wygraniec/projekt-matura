package pl.lodz.p.liceum.matura.api.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
