package ru.netology.cloudservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.netology.cloudservice.dto.FileNameInRequest;
import ru.netology.cloudservice.dto.LoginInRequest;
import ru.netology.cloudservice.dto.LoginInResponse;
import ru.netology.cloudservice.entities.FileEntity;
import ru.netology.cloudservice.entities.TokenEntity;
import ru.netology.cloudservice.entities.UserEntity;
import ru.netology.cloudservice.repository.FileRepository;
import ru.netology.cloudservice.repository.TokenRepository;
import ru.netology.cloudservice.repository.UserRepository;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
class CloudServiceApplicationTests {
    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        fileRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @Test
    public void testLogin() {
        userRepository.save(new UserEntity("pikalov", "password"));
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        final LoginInRequest operation = new LoginInRequest("pikalov", "password");
        final HttpEntity<LoginInRequest> request = new HttpEntity<>(operation, headers);

        final ResponseEntity<LoginInResponse> result = this.restTemplate.postForEntity("/login", request, LoginInResponse.class);
        Assertions.assertNotNull(result.getBody());
        Assertions.assertNotNull(result.getBody().getAuthToken());
    }

    @Test
    public void testLogout() {
        final String authToken = "Bearer 777";
        tokenRepository.save(new TokenEntity(authToken));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);
        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        this.restTemplate.postForEntity("/logout", request, Void.class);
        Assertions.assertFalse(tokenRepository.existsById(authToken.split(" ")[1].trim()));
    }

    @Test
    public void testUploadFile() {
        final String authToken = "Bearer 777";
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
        parts.add("file", new ClassPathResource("testing.txt"));

        final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parts, headers);

        this.restTemplate.postForEntity("/file?filename=testing.txt", request, Void.class);

        final Optional<FileEntity> fileInRepository = fileRepository.findById("testing.txt");
        Assertions.assertTrue(fileInRepository.isPresent());
        Assertions.assertEquals(new FileEntity("testing.txt", new byte[]{49, 51, 50}), fileInRepository.get());
    }

    @Test
    public void testDeleteFile() {
        fileRepository.save(new FileEntity("testing.txt", new byte[]{49, 51, 50}));

        final String authToken = "Bearer 777";
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        this.restTemplate.exchange("/file?filename=testing.txt", HttpMethod.DELETE, request, Void.class);

        Assertions.assertFalse(fileRepository.existsById("testing.txt"));
    }

    @Test
    public void testGetFile() {
        fileRepository.save(new FileEntity("testing.txt", new byte[]{49, 51, 50}));

        final String authToken = "Bearer 777";
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        final ResponseEntity<byte[]> result = this.restTemplate.exchange("/file?filename=testing.txt", HttpMethod.GET, request, byte[].class);

        Assertions.assertNotNull(result.getBody());
        Assertions.assertArrayEquals(new byte[]{49, 51, 50}, result.getBody());
    }

    @Test
    public void testEditFile() {
        fileRepository.save(new FileEntity("testing.txt", new byte[]{49, 51, 50}));

        final String authToken = "Bearer 777";
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<FileNameInRequest> request =
                new HttpEntity<>(new FileNameInRequest("testFile.txt"), headers);

        this.restTemplate.exchange("/file?filename=testing.txt", HttpMethod.PUT, request, Void.class);

        Assertions.assertFalse(fileRepository.existsById("testing.txt"));
        final Optional<FileEntity> fileInRepository = fileRepository.findById("testFile.txt");
        Assertions.assertTrue(fileInRepository.isPresent());
        Assertions.assertEquals(new FileEntity("testFile.txt", new byte[]{49, 51, 50}), fileInRepository.get());
    }

    @Test
    public void testGetFileList() {
        fileRepository.save(new FileEntity("testing.txt", new byte[]{49, 51, 50}));

        final String authToken = "Bearer 777";
        tokenRepository.save(new TokenEntity(authToken.split(" ")[1].trim()));

        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", authToken);

        final HttpEntity<Void> request = new HttpEntity<>(null, headers);

        final ResponseEntity<Object> result = this.restTemplate.exchange("/list?limit=10", HttpMethod.GET, request, Object.class);

        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals("[{filename=testing.txt, size=3}]", result.getBody().toString());
    }
}
