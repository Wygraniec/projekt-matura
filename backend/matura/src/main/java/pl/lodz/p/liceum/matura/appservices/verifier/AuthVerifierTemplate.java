package pl.lodz.p.liceum.matura.appservices.verifier;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import pl.lodz.p.liceum.matura.appservices.IAuthenticationFacade;
import pl.lodz.p.liceum.matura.domain.template.Template;
import pl.lodz.p.liceum.matura.domain.template.TemplateService;
import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.domain.user.UserService;

import java.util.Arrays;

import static pl.lodz.p.liceum.matura.domain.user.UserRole.ADMIN;


@Component
@Aspect
@RequiredArgsConstructor
public class AuthVerifierTemplate {

    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final TemplateService templateService;


    @Before(value = "@annotation(authVerifyTemplate)")
    public void userIsAdminOwnerOfTemplate(JoinPoint joinPoint, AuthVerifyTemplate authVerifyTemplate) {
        Integer templateId = (Integer) getParameterByName(joinPoint, authVerifyTemplate.templateIdParamName());
        User user = userService.findById(authenticationFacade.getLoggedInUserId());

        Template template;
        try {
            template = templateService.findById(templateId);
        } catch (Exception ex) {
            throw new UserIsNotAuthorizedToThisTemplateException("User is not authorized to this template.");
        }

        final boolean userIsOwnerOfThisTemplate = template.isUserTheOwnerOfThisTemplate(user.getId());
        final boolean userIsAdmin = user.getRole().equals(ADMIN);
        boolean authorizationConfirmed = userIsOwnerOfThisTemplate || userIsAdmin;

        if (!authorizationConfirmed) {
            throw new UserIsNotAuthorizedToThisTemplateException("User is not authorized to this template.");
        }
    }

    private Object getParameterByName(JoinPoint joinPoint, String parameterName) {
        MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        if (args.length==0){
            throw new UserIsNotAuthorizedToThisTemplateException("Parameter " + parameterName + " not provided.");
        }
        String[] parametersName = methodSig.getParameterNames();
        int index = Arrays.asList(parametersName).indexOf(parameterName);
        return args[index];
    }

}
