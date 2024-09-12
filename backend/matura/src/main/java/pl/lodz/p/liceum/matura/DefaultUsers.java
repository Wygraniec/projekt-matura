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
            0,
            ZonedDateTime.now()
    );

    private final User studentUser = new User(
            null,
            "james@gmail.com",
            "James",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );

    private final User instructorUser = new User(
            null,
            "mary@gmail.com",
            "Mary",
            "password",
            UserRole.INSTRUCTOR,
            0,
            ZonedDateTime.now()
    );
    private final User test1 = new User(
            null,
            "hubert@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test2 = new User(
            null,
            "wojciechl@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test3 = new User(
            null,
            "krzysztof@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test4 = new User(
            null,
            "jakub@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test5 = new User(
            null,
            "oskar@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test6 = new User(
            null,
            "ola@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test7 = new User(
            null,
            "tomasz@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test8 = new User(
            null,
            "pawel@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test9 = new User(
            null,
            "dawid@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test10 = new User(
            null,
            "laura@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test11 = new User(
            null,
            "miachal@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test12 = new User(
            null,
            "tymek@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );
    private final User test13 = new User(
            null,
            "wojciechg@gmail.com",
            "Test",
            "password",
            UserRole.STUDENT,
            0,
            ZonedDateTime.now()
    );

    @Override
    public void run(String... args) {
        try {
            addUser(adminUser);
            addUser(studentUser);
            addUser(instructorUser);
            addUser(test1);
            addUser(test2);
            addUser(test3);
            addUser(test4);
            addUser(test5);
            addUser(test6);
            addUser(test7);
            addUser(test8);
            addUser(test9);
            addUser(test10);
            addUser(test11);
            addUser(test12);
            addUser(test13);
        } catch (Exception ex) {
            log.warning(ex.getMessage());
        }
    }

    private void addUser(User user) {
        userService.save(user);
    }
}
