package pl.lodz.p.liceum.matura;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.lodz.p.liceum.matura.domain.user.User;
import pl.lodz.p.liceum.matura.domain.user.UserRole;
import pl.lodz.p.liceum.matura.domain.user.UserService;
import pl.lodz.p.liceum.matura.external.storage.user.JpaUserRepository;
import pl.lodz.p.liceum.matura.security.JWTUtil;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ActiveProfiles("it")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MaturaApplication.class
)
@ExtendWith(SpringExtension.class)
public class BaseIT {

    @Autowired
    protected Environment environment;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected UserService userService;

    protected BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected JWTUtil jwtUtil;

    @Autowired
    private ServerPortService serverPortService;

    @Autowired
    private JpaUserRepository jpaUserRepository;
    @BeforeEach
    void init() {
        jpaUserRepository.deleteAll();
        addTestUsers();
    }

    private User adminUser = new User(
            null,
            "admin@gmail.com",
            "John",
            "password",
            UserRole.ADMIN,
            ZonedDateTime.of(
                    2023,
                    6,
                    17,
                    12,
                    30,
                    20,
                    0,
                    ZoneId.of("UTC"))
    );

    private User instructorUser = new User(
            null,
            "instructor@gmail.com",
            "John",
            "password",
            UserRole.INSTRUCTOR,
            ZonedDateTime.of(
                    2023,
                    6,
                    17,
                    12,
                    30,
                    20,
                    0,
                    ZoneId.of("UTC"))
    );

    private User studentUser = new User(
            null,
            "student@gmail.com",
            "John",
            "password",
            UserRole.STUDENT,
            ZonedDateTime.of(
                    2023,
                    6,
                    17,
                    12,
                    30,
                    20,
                    0,
                    ZoneId.of("UTC"))
    );

    protected String localUrl(String endpoint) {
        int port = serverPortService.getPort();
        return "http://localhost:" + port + endpoint;
    }

    protected void addTestUsers() {
        userService.save(adminUser);
        userService.save(instructorUser);
        userService.save(studentUser);
    }

    protected String getAccessTokenForUser(User user) {
        String token = jwtUtil.issueToken(user.getEmail(), "ROLE_" + user.getRole());
        return "Bearer " + token;
    }

    protected String getAccessTokenForAdmin() {
        String token = jwtUtil.issueToken(adminUser.getEmail(), "ROLE_" + adminUser.getRole());
        return "Bearer " + token;
    }

    protected String getAccessTokenForInstructor() {
        String token = jwtUtil.issueToken(instructorUser.getEmail(), "ROLE_" + instructorUser.getRole());
        return "Bearer " + token;
    }

    protected String getAccessTokenForStudent() {
        String token = jwtUtil.issueToken(studentUser.getEmail(), "ROLE_" + studentUser.getRole());
        return "Bearer " + token;
    }

    protected <T, U> ResponseEntity<U> callHttpMethod(
            HttpMethod httpMethod,
            String url,
            String accessToken,
            T body,
            Class<U> mapToObject
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);
        headers.add(HttpHeaders.ACCEPT, "application/json");
        HttpEntity<T> requestEntity;
        if (body == null) {
            requestEntity = new HttpEntity<>(headers);
        } else {
            requestEntity = new HttpEntity<>(body, headers);
        }
        return restTemplate.exchange(
                localUrl(url),
                httpMethod,
                requestEntity,
                mapToObject
        );
    }

}
