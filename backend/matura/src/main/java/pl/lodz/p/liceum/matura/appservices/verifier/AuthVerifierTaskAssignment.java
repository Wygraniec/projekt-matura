package pl.lodz.p.liceum.matura.appservices.verifier;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import pl.lodz.p.liceum.matura.appservices.IAuthenticationFacade;
import pl.lodz.p.liceum.matura.domain.task.Task;
import pl.lodz.p.liceum.matura.domain.task.TaskService;
import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.domain.user.UserService;

import java.util.Arrays;

import static pl.lodz.p.liceum.matura.domain.user.UserRole.ADMIN;
import static pl.lodz.p.liceum.matura.domain.user.UserRole.INSTRUCTOR;


@Component
@Aspect
@RequiredArgsConstructor
public class AuthVerifierTaskAssignment {

    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;
    private final TaskService taskService;


    @Before(value = "@annotation(authVerifyTaskAssignment)")
    public void userHavePermissionToAssignThisTask(JoinPoint joinPoint, AuthVerifyTaskAssignment authVerifyTaskAssignment) {
        Task task = (Task) getParameterByName(joinPoint, authVerifyTaskAssignment.taskParamName());
        User user = userService.findById(authenticationFacade.getLoggedInUserId());

        final boolean userIsAssignedToThisTask = task.isUserAssignedToThisTask(user.getId());
        final boolean userIsInstructor = user.getRole().equals(INSTRUCTOR);
        final boolean userIsAdmin = user.getRole().equals(ADMIN);
        boolean authorizationConfirmed = userIsAssignedToThisTask || userIsInstructor || userIsAdmin;

        if (!authorizationConfirmed) {
            throw new UserIsNotAuthorizedToThisTaskException("User is not authorized to this task.");
        }
    }

    private Object getParameterByName(JoinPoint joinPoint, String parameterName) {
        MethodSignature methodSig = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        if (args.length==0){
            throw new UserIsNotAuthorizedToThisTaskException("Parameter " + parameterName + " not provided.");
        }
        String[] parametersName = methodSig.getParameterNames();
        int index = Arrays.asList(parametersName).indexOf(parameterName);
        return args[index];
    }

}
