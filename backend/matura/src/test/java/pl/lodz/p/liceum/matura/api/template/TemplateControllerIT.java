package pl.lodz.p.liceum.matura.api.template;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import pl.lodz.p.liceum.matura.BaseIT;
import pl.lodz.p.liceum.matura.TestTemplateFactory;
import pl.lodz.p.liceum.matura.TestUserFactory;
import pl.lodz.p.liceum.matura.api.response.ErrorResponse;
import pl.lodz.p.liceum.matura.api.user.PageUserDto;
import pl.lodz.p.liceum.matura.api.user.UserDto;
import pl.lodz.p.liceum.matura.appservices.TaskStatementApplicationService;
import pl.lodz.p.liceum.matura.domain.template.TaskLanguage;
import pl.lodz.p.liceum.matura.domain.template.Template;
import pl.lodz.p.liceum.matura.domain.template.TemplateService;
import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.domain.user.UserService;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.notNull;

class TemplateControllerIT extends BaseIT {

    @Autowired
    TemplateService service;
    @MockBean
    TaskStatementApplicationService taskStatementApplicationService;

    @Test
    void admin_should_get_information_about_any_template() {
        //given
        Template template = TestTemplateFactory.createJavaTemplate();
        service.save(template);
        String token = getAccessTokenForAdmin();

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/api/v1/templates/" + service.findBySourceUrl(template.getSourceUrl()).getId(),
                token,
                null,
                TemplateDto.class);

        //then
        TemplateDto body = response.getBody();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNotNull(body);
        assertEquals(template.getSourceUrl(), body.sourceUrl());
        assertEquals(template.getTaskLanguage().toString(), body.taskLanguage());
        assertEquals(template.getSource(), body.source());
    }

    @Test
    void admin_should_get_response_code_404_when_template_does_not_exist_in_db() {
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
    void user_should_not_get_information_about_any_template() {
        //given
        User user = TestUserFactory.createInstructor();
        Template template = TestTemplateFactory.createCSharpTemplate();
        userService.save(user);
        service.save(template);
        String accessToken = getAccessTokenForUser(user);

        //when
        var response = callHttpMethod(HttpMethod.GET,
                "/api/v1/users/" + service.findBySourceUrl(template.getSourceUrl()).getId(),
                accessToken,
                null,
                ErrorResponse.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
    }

    @Test
    void admin_should_be_able_to_save_new_template() {
        Mockito.when(taskStatementApplicationService.readTaskStatement((Template) notNull())).thenReturn("Test task statement");
        //given
        Template template = TestTemplateFactory.createCSharpTemplate();
        User admin = userService.save(TestUserFactory.createAdmin());
        String token = jwtUtil.issueToken(admin.getEmail(), "ROLE_" + admin.getRole());
        String adminAccessToken = "Bearer " + token;

        //when
        var timeBeforeCallingTheHTTPMethod = ZonedDateTime.now();
        var response = callHttpMethod(HttpMethod.POST,
                "/api/v1/templates",
                adminAccessToken,
                template,
                TemplateDto.class);

        //then
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        //and
        TemplateDto body = response.getBody();
        assertNotNull(body);
        assertEquals(template.getSourceUrl(), body.sourceUrl());
        assertEquals(template.getTaskLanguage().toString(), body.taskLanguage());
        assertEquals(template.getSource(), body.source());
        assertTrue(timeBeforeCallingTheHTTPMethod.isBefore(body.createdAt()));
        assertEquals(body.createdBy(), admin.getId());
    }

    @Test
    void admin_should_be_able_to_update_template() {
        //given
        Template template = TestTemplateFactory.createPythonTemplate();
        service.save(template);
        Template toUpdate = new Template(
                template.getId(),
                "newUrl.example.com",
                TaskLanguage.JAVA,
                "Matura 07.2023",
                "Updated task statement",
                3,
                template.getCreatedBy(),
                template.getCreatedAt()
        );
        String adminAccessToken = getAccessTokenForAdmin();

        //when
        var response = callHttpMethod(HttpMethod.PUT,
                "/api/v1/templates",
                adminAccessToken,
                toUpdate,
                TemplateDto.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void admin_should_get_response_code_200_when_update_template_does_not_exist() {
        //given
        String token = getAccessTokenForAdmin();
        Template fakeTemplate = TestTemplateFactory.createCSharpTemplate();

        //when
        var response = callHttpMethod(HttpMethod.PUT,
                "/api/v1/templates",
                token,
                fakeTemplate,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void user_should_not_be_able_to_update_template() {
        //given
        User user = TestUserFactory.createStudent();
        userService.save(user);
        Template template = TestTemplateFactory.createPythonTemplate();
        service.save(template);
        Template toUpdate = new Template(
                template.getId(),
                "newUrl.example.com",
                TaskLanguage.JAVA,
                "Matura 07.2023",
                "Updated task statement",
                1,
                template.getCreatedBy(),
                template.getCreatedAt()
        );
        String token = getAccessTokenForUser(user);

        //when
        var response = callHttpMethod(HttpMethod.PUT,
                "/api/v1/templates",
                token,
                toUpdate,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
    @Test
    void instructor_should_be_able_to_update_template() {
        //given
        User user = TestUserFactory.createInstructor();
        userService.save(user);

        Template template = TestTemplateFactory.createPythonTemplate();
        service.save(template);
        Template toUpdate = new Template(
                template.getId(),
                "newUrl.example.com",
                TaskLanguage.JAVA,
                "Matura 07.2023",
                "Updated task statement",
                1,
                template.getCreatedBy(),
                template.getCreatedAt()
        );
        String token = getAccessTokenForUser(user);

        //when
        var response = callHttpMethod(HttpMethod.PUT,
                "/api/v1/templates",
                token,
                toUpdate,
                TemplateDto.class);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void admin_should_be_able_to_delete_template() {
        //given
        Template template = TestTemplateFactory.createPythonTemplate();
        String adminAccessToken = getAccessTokenForAdmin();
        service.save(template);

        //when
        var response = callHttpMethod(
                HttpMethod.DELETE,
                "/api/v1/templates/" + service.findBySourceUrl(template.getSourceUrl()).getId(),
                adminAccessToken,
                null,
                TemplateDto.class);

        //then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void admin_should_get_response_code_204_when_template_does_not_exist() {
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

    @Test
    void instructor_should_be_able_to_delete_his_template() {
        //given
        User user = userService.save(TestUserFactory.createInstructor());
        Template template = TestTemplateFactory.createJavaTemplate();
        template.setCreatedBy(user.getId());
        final Template savedTemplate = service.save(template);
        String token = getAccessTokenForUser(user);

        //when
        var response = callHttpMethod(
                HttpMethod.DELETE,
                "/api/v1/templates/" + savedTemplate.getId(),
                token,
                null,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void instructor_should_not_be_able_to_delete_someone_else_template() {
        //given
        User user = userService.save(TestUserFactory.createInstructor());
        Template template = TestTemplateFactory.createJavaTemplate();
        template.setCreatedBy(user.getId() + 1);
        service.save(template);
        String token = getAccessTokenForUser(user);

        //when
        var response = callHttpMethod(
                HttpMethod.DELETE,
                "/api/v1/templates/" + template.getId(),
                token,
                null,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
    }

    @Test
    void student_should_not_be_able_to_delete_template() {
        //given
        User user = TestUserFactory.createStudent();
        Template template = TestTemplateFactory.createJavaTemplate();
        userService.save(user);
        service.save(template);
        String token = getAccessTokenForUser(user);

        //when
        var response = callHttpMethod(
                HttpMethod.DELETE,
                "/api/v1/templates/" + template.getId(),
                token,
                null,
                ErrorResponse.class);

        //then
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

//    @Test
//    void admin_should_get_pageable_list_of_users() {
//        //give
//        User user1 = TestUserFactory.createInstructor();
//        User user2 = TestUserFactory.createInstructor();
//        User user3 = TestUserFactory.createInstructor();
//        User user4 = TestUserFactory.createInstructor();
//        String adminAccessToken = getAccessTokenForAdmin();
//        userService.save(user1);
//        userService.save(user2);
//        userService.save(user3);
//        userService.save(user4);
//
//        //when
//        var response = callHttpMethod(
//                HttpMethod.GET,
//                "/api/v1/users",
//                adminAccessToken,
//                null,
//                PageUserDto.class
//        );
//
//        //then
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        PageUserDto body = response.getBody();
//        //and
//        assertNotNull(body);
//        assertEquals(6, body.users().size());
//        assertEquals(7, body.totalElements());
//        assertEquals(2, body.totalPages());
//        assertEquals(1, body.currentPage());
//        //and users passwords should be hashed
//        assertTrue(
//                body.users().stream()
//                        .allMatch(userDto -> userDto.password().equals("######"))
//        );
//    }

}
