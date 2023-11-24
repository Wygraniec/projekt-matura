package pl.lodz.p.liceum.matura.domain.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.lodz.p.liceum.matura.BaseIT;
import pl.lodz.p.liceum.matura.TestUserFactory;

public class UserServiceIT extends BaseIT {

    @Autowired
    UserService service;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void add_user_test() {
        //given
        User user = TestUserFactory.createInstructor();
        service.save(user);

        //when
        User readUser = service.findByEmail(user.getEmail());

        //then
        Assertions.assertEquals(user.getEmail(), readUser.getEmail());
        Assertions.assertEquals(user.getNickname(), readUser.getNickname());
        Assertions.assertTrue(passwordEncoder.matches(user.getPassword(), readUser.getPassword()));
    }

    @Test
    void get_by_email_should_return_correct_user() {
        //given
        User user1 = TestUserFactory.createInstructor();
        User user2 = TestUserFactory.createStudent();
        User user3 = TestUserFactory.createAdmin();
        service.save(user1);
        service.save(user2);
        service.save(user3);

        //when
        User readUser = service.findByEmail(user2.getEmail());

        //then
        Assertions.assertEquals(user2.getEmail(), readUser.getEmail());
        Assertions.assertEquals(user2.getNickname(), readUser.getNickname());
        Assertions.assertTrue(passwordEncoder.matches(user2.getPassword(), readUser.getPassword()));
    }
}