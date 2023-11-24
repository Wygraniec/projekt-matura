package pl.lodz.p.liceum.matura;


import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.domain.user.UserRole;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TestUserFactory {

    private static int userSequence = 10;

    public static User createAdmin() {
        userSequence++;
        return new User(
                userSequence,
                "newUser" + userSequence + "@example.com",
                "User Name " + userSequence,
                "password",
                UserRole.ADMIN,
                ZonedDateTime.of(2023, 6, 17, 12, 40, 00, 0, ZoneId.of("UTC"))
        );
    }

    public static User createInstructor() {
        userSequence++;
        return new User(
                userSequence,
                "newUser" + userSequence + "@example.com",
                "User Name " + userSequence,
                "password",
                UserRole.INSTRUCTOR,
                ZonedDateTime.of(2023, 6, 17, 12, 40, 00, 0, ZoneId.of("UTC"))
        );
    }

    public static User createStudent() {
        userSequence++;
        return new User(
                userSequence,
                "newUser" + userSequence + "@example.com",
                "User Name " + userSequence,
                "password",
                UserRole.STUDENT,
                ZonedDateTime.of(2023, 6, 17, 12, 40, 00, 0, ZoneId.of("UTC"))
        );
    }
}
