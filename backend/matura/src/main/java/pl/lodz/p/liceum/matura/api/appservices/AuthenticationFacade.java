package pl.lodz.p.liceum.matura.api.appservices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.domain.user.UserService;
import pl.lodz.p.liceum.matura.security.UserDetailsImpl;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade implements IAuthenticationFacade {
    private final UserService userService;
    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public Integer getLoggedInUserId() {
        Authentication authentication = getAuthentication();
        User user = userService.findByEmail(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
        return user.getId();
    }

}
