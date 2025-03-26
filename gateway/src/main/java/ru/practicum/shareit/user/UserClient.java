package ru.practicum.shareit.user;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserDto;

@Component
public class UserClient {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    public UserClient(@Value("${SHAREIT_SERVER_URL:http://localhost:9090}") String serverUrl) {
        this.serverUrl = serverUrl + "/users";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        this.restTemplate = new RestTemplate(requestFactory);
    }

    public ResponseEntity<Object> createUser(UserDto userDto) {
        return restTemplate.postForEntity(serverUrl, userDto, Object.class);
    }

    public ResponseEntity<Object> getAllUsers() {
        return restTemplate.getForEntity(serverUrl, Object.class);
    }

    public ResponseEntity<Object> getUserById(Long id) {
        return restTemplate.getForEntity(serverUrl + "/" + id, Object.class);
    }

    public ResponseEntity<Object> updateUser(Long id, UserDto userDto) {
        return restTemplate.exchange(
                serverUrl + "/" + id,
                HttpMethod.PATCH,
                new HttpEntity<>(userDto),
                Object.class
        );
    }

    public ResponseEntity<Object> deleteUser(Long id) {
        restTemplate.delete(serverUrl + "/" + id);
        return ResponseEntity.noContent().build();
    }
}