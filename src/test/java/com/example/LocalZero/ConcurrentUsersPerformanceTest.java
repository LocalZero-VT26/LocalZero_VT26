package com.example.LocalZero;
//.\mvnw.cmd test -Dtest=ConcurrentUsersPerformanceTest
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ConcurrentUsersPerformanceTest {

    private static final int CONCURRENT_USERS = 10;
    private static final String[] LOCATIONS = {
            "Stockholm", "Gothenburg", "Malmo", "Uppsala",
            "Stockholm", "Gothenburg", "Malmo", "Uppsala",
            "Stockholm", "Gothenburg"
    };

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private JavaMailSender javaMailSender;

    @BeforeEach
    void stubJavaMailSender() {
        Session session = Session.getInstance(new Properties());
        when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage(session));
    }

    @Test
    void tenConcurrentUsersCanRegisterLoginAndBrowseInitiatives() throws Exception {
        String baseUrl = "http://localhost:" + port;
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_USERS);
        List<Callable<String>> tasks = new ArrayList<>();

        for (int i = 0; i < CONCURRENT_USERS; i++) {
            final int userId = i;
            tasks.add(() -> simulateUserSession(baseUrl, userId));
        }

        List<Future<String>> results = executor.invokeAll(tasks);
        executor.shutdown();
        assertThat(executor.awaitTermination(30, TimeUnit.SECONDS)).isTrue();

        for (Future<String> result : results) {
            assertThat(result.get()).startsWith("concurrent-user-");
        }
    }

    private String simulateUserSession(String baseUrl, int userId) {
        String email = "concurrent-user-" + userId + "@localzero.test";
        String password = "password123";

        Map<String, Object> registerBody = Map.of(
                "name", "User " + userId,
                "email", email,
                "password", password,
                "location", LOCATIONS[userId]
        );
        ResponseEntity<Map> registerResponse = restTemplate.postForEntity(
                baseUrl + "/auth/register", registerBody, Map.class);
        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Map<String, String> loginBody = Map.of("email", email, "password", password);
        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(
                baseUrl + "/auth/login", loginBody, Map.class);
        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).isNotNull();

        String token = (String) loginResponse.getBody().get("token");
        assertThat(token).isNotBlank();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> authEntity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> initiativesResponse = restTemplate.exchange(
                baseUrl + "/api/initiatives",
                HttpMethod.GET,
                authEntity,
                new ParameterizedTypeReference<>() {
                });
        assertThat(initiativesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<List<Map<String, Object>>> updatesResponse = restTemplate.exchange(
                baseUrl + "/api/initiatives/updates",
                HttpMethod.GET,
                authEntity,
                new ParameterizedTypeReference<>() {
                });
        assertThat(updatesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        return email;
    }
}
