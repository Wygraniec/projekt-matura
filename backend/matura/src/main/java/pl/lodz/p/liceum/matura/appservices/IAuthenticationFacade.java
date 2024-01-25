package pl.lodz.p.liceum.matura.appservices;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
    Integer getLoggedInUserId();
}