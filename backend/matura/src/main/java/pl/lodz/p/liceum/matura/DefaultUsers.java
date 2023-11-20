package pl.lodz.p.liceum.matura;

import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.domain.user.UserRole;
import pl.lodz.p.liceum.matura.domain.user.UserService;

import java.time.ZonedDateTime;

@Component
@Log
public class DefaultUsers implements CommandLineRunner {

    private final UserService userService;

    public DefaultUsers(UserService userService) {
        this.userService = userService;
    }

    private final User adminUser = new User(
            null,
            "john@gmail.com",
            "John",
            "password",
            UserRole.ADMIN,
            ZonedDateTime.now()
    );

    private final User studentUser = new User(
            null,
            "james@gmail.com",
            "James",
            "password",
            UserRole.STUDENT,
            ZonedDateTime.now()
    );

    private final User instructorUser = new User(
            null,
            "mary@gmail.com",
            "Mary",
            "password",
            UserRole.INSTRUCTOR,
            ZonedDateTime.now()
    );

    @Override
    public void run(String... args) {
        try {
            addUser(adminUser);
            addUser(studentUser);
            addUser(instructorUser);
        } catch (Exception ex) {
            log.warning(ex.getMessage());
        }
    }

    private void addUser(User user) {
        userService.save(user);
    }
}
