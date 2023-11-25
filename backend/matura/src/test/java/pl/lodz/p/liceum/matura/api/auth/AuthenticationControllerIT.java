package pl.lodz.p.liceum.matura.api.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.lodz.p.liceum.matura.BaseIT;
import pl.lodz.p.liceum.matura.TestUserFactory;
import pl.lodz.p.liceum.matura.api.response.ErrorResponse;
import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.domain.user.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthenticationControllerIT extends BaseIT {

    @Autowired
    UserService userService;

    @Test
    void user_should_get_response_code_not_found_when_user_has_no_access() {
        //given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("terrence","password");

        //when
        var response = callHttpMethod(HttpMethod.POST,
                "/api/v1/auth/login",
                null,
                authenticationRequest,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void user_should_get_response_code_success_when_user_has_access() {
        //given
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("admin@gmail.com","password");

        //when
        var response = callHttpMethod(HttpMethod.POST,
                "/api/v1/auth/login",
                null,
                authenticationRequest,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void user_should_get_response_code_success_when_user_has_access_and_is_an_instructor() {
        //given

        User user1 = TestUserFactory.createInstructor();
        userService.save(user1);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(user1.getEmail(),user1.getPassword());

        //when
        var response = callHttpMethod(HttpMethod.POST,
                "/api/v1/auth/login",
                null,
                authenticationRequest,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void user_should_get_response_code_success_when_user_has_access_and_is_a_student() {
        //given

        User user1 = TestUserFactory.createStudent();
        userService.save(user1);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(user1.getEmail(),user1.getPassword());

        //when
        var response = callHttpMethod(HttpMethod.POST,
                "/api/v1/auth/login",
                null,
                authenticationRequest,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void user_should_get_response_code_forbidden_when_password_is_incorrect() {
        //given

        User user1 = TestUserFactory.createInstructor();
        userService.save(user1);

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(user1.getEmail(),"bad_password");

        //when
        var response = callHttpMethod(HttpMethod.POST,
                "/api/v1/auth/login",
                null,
                authenticationRequest,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}