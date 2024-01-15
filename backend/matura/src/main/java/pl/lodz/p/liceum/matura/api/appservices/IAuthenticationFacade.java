package pl.lodz.p.liceum.matura.api.appservices;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    Integer getLoggedInUserId();
}