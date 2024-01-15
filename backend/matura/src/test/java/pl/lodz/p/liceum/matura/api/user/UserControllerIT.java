package pl.lodz.p.liceum.matura.api.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.lodz.p.liceum.matura.BaseIT;
import pl.lodz.p.liceum.matura.TestUserFactory;
import pl.lodz.p.liceum.matura.api.response.ErrorResponse;
import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.domain.user.UserService;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerIT extends BaseIT {

    @Autowired
    UserService service;

    @Test
    void admin_should_get_information_about_any_user() {
        //given
        User user = TestUserFactory.createInstructor();
        service.save(user);
        String token = getAccessTokenForAdmin();

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/api/v1/users/" + service.findByEmail(user.getEmail()).getId(),
                token,
                null,
                UserDto.class);

        //then
        UserDto body = response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(body);
        assertEquals(user.getEmail(), body.email());
        assertEquals(user.getUsername(), body.username());
        assertEquals("######", body.password());
        assertEquals(user.getRole().toString(), body.role());
    }

    @Test
    void admin_should_get_response_code_404_when_user_not_exits_in_db() {
        //given
        String token = getAccessTokenForAdmin();

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/api/v1/users/10",
                token,
                null,
                ErrorResponse.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    void user_should_not_get_information_about_other_user() {
        //given
        User user1 = TestUserFactory.createInstructor();
        User user2 = TestUserFactory.createStudent();
        service.save(user1);
        service.save(user2);
        String accessToken = getAccessTokenForUser(user1);

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/api/v1/users/" + service.findByEmail(user2.getEmail()).getId(),
                accessToken,
                null,
                ErrorResponse.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

//    @Test
//    void admin_should_get_response_code_conflict_when_user_is_in_db() {
//        //given
//        User user = TestUserFactory.createInstructor();
//        service.save(user);
//        String adminToken = getAccessTokenForAdmin();
//
//        //when
//        var response = callHttpMethod(HttpMethod.POST,
//                "/api/v1/users",
//                adminToken,
//                user,
//                ErrorResponse.class);
//
//        //then
//        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
//    }


    @Test
    void admin_should_be_able_to_save_new_user() {
        //given
        User user = TestUserFactory.createInstructor();
        User admin = userService.save(TestUserFactory.createAdmin());
        String token = jwtUtil.issueToken(admin.getEmail(), "ROLE_" + admin.getRole());
        String adminAccessToken = "Bearer " + token;

        //when
        var timeBeforeCallingTheHTTPMethod = ZonedDateTime.now();
        var response = callHttpMethod(HttpMethod.POST,
                "/api/v1/users",
                adminAccessToken,
                user,
                UserDto.class);

        //then
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        //and
        UserDto body = response.getBody();
        assertNotNull(body);
        assertEquals(body.email(), user.getEmail());
        assertEquals(body.username(), user.getUsername());
        assertEquals(body.password(), "######");
        assertEquals(body.role(), user.getRole().toString());
        //assertEquals(body.createdAt().format(formatter), timeBeforeCallingTheHTTPMethod.format(formatter));
        assertTrue(timeBeforeCallingTheHTTPMethod.isBefore(body.createdAt()));
        assertEquals(body.createdBy(), admin.getId());
    }

    @Test
    void user_should_get_information_about_himself() {
        //given
        User user = TestUserFactory.createInstructor();
        service.save(user);
        String accessToken = getAccessTokenForUser(user);

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/api/v1/users/me",
                accessToken,
                null,
                UserDto.class);

        //then
        UserDto body = response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        //and
        assertNotNull(body);
        assertEquals(body.email(), user.getEmail());
        assertEquals(body.username(), user.getUsername());
        assertEquals(body.password(), "######");
        assertEquals(body.role(), user.getRole().toString());
    }

    @Test
    void admin_should_be_able_to_update_user() {
        //given
        User user = TestUserFactory.createInstructor();
        userService.save(user);
        User toUpdate = new User(
                user.getId(),
                "email@email.com",
                "newPerson",
                "newpassword",
                user.getRole(),
                user.getCreatedBy(),
                user.getCreatedAt()
        );
        String adminAccessToken = getAccessTokenForAdmin();

        //when
        var response = callHttpMethod(HttpMethod.PUT,
                "/api/v1/users",
                adminAccessToken,
                toUpdate,
                UserDto.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void admin_should_get_response_code_200_when_update_user_not_exits() {
        //given
        String token = getAccessTokenForAdmin();
        User fakeUser = TestUserFactory.createInstructor();

        //when
        var response = callHttpMethod(HttpMethod.PUT,
                "/api/v1/users",
                token,
                fakeUser,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void user_should_not_be_able_to_update_user() {
        //given
        User user = TestUserFactory.createInstructor();
        userService.save(user);
        User userToUpdate = new User(
                user.getId(),
                "otherUser@email.com",
                "Person",
                "password",
                user.getRole(),
                user.getCreatedBy(),
                user.getCreatedAt()
        );
        String token = getAccessTokenForUser(user);

        //when
        var response = callHttpMethod(HttpMethod.PUT,
                "/api/v1/users",
                token,
                userToUpdate,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void admin_should_be_able_to_delete_user() {
        //given
        User user = TestUserFactory.createInstructor();
        String adminAccessToken = getAccessTokenForAdmin();
        userService.save(user);

        //when
        var response = callHttpMethod(
                HttpMethod.DELETE,
                "/api/v1/users/" + service.findByEmail(user.getEmail()).getId(),
                adminAccessToken,
                null,
                UserDto.class);

        //then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void admin_should_get_response_code_204_when_user_not_exits() {
        //given
        User user = TestUserFactory.createInstructor();
        String token = getAccessTokenForAdmin();

        //when
        var response = callHttpMethod(
                HttpMethod.DELETE,
                "/api/v1/users/" + user.getId(),
                token,
                null,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
    //
    @Test
    void instructor_should_not_be_able_to_delete_user() {
        //given
        User firstUser = TestUserFactory.createInstructor();
        User secondUser = TestUserFactory.createStudent();
        userService.save(firstUser);
        userService.save(secondUser);
        String token = getAccessTokenForUser(firstUser);

        //when
        var response = callHttpMethod(
                HttpMethod.DELETE,
                "/api/v1/users/" + secondUser.getId(),
                token,
                null,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
    @Test
    void student_should_not_be_able_to_delete_user() {
        //given
        User firstUser = TestUserFactory.createStudent();
        User secondUser = TestUserFactory.createStudent();
        userService.save(firstUser);
        userService.save(secondUser);
        String token = getAccessTokenForUser(firstUser);

        //when
        var response = callHttpMethod(
                HttpMethod.DELETE,
                "/api/v1/users/" + secondUser.getId(),
                token,
                null,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void admin_should_get_pageable_list_of_users() {
        //give
        User user1 = TestUserFactory.createInstructor();
        User user2 = TestUserFactory.createInstructor();
        User user3 = TestUserFactory.createInstructor();
        User user4 = TestUserFactory.createInstructor();
        String adminAccessToken = getAccessTokenForAdmin();
        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        userService.save(user4);

        //when
        var response = callHttpMethod(
                HttpMethod.GET,
                "/api/v1/users",
                adminAccessToken,
                null,
                PageUserDto.class
        );

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        PageUserDto body = response.getBody();
        //and
        assertNotNull(body);
        assertEquals(6, body.users().size());
        assertEquals(7, body.totalElements());
        assertEquals(2, body.totalPages());
        assertEquals(1, body.currentPage());
        //and users passwords should be hashed
        assertTrue(
                body.users().stream()
                        .allMatch(userDto -> userDto.password().equals("######"))
        );
    }

}
