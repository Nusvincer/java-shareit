package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserDto;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class UserClientTest {

    private UserClient userClient;
    private MockRestServiceServer server;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        RestTemplate testRestTemplate = new RestTemplate();
        userClient = new UserClient("http://localhost:9090");
        Field field = UserClient.class.getDeclaredField("restTemplate");
        field.setAccessible(true);
        field.set(userClient, testRestTemplate);

        server = MockRestServiceServer.createServer(testRestTemplate);
    }

    @Test
    void createUser_shouldReturnOkResponse() throws Exception {
        UserDto userDto = new UserDto(null, "Алексей", "alex@example.com");
        String json = objectMapper.writeValueAsString(userDto);

        server.expect(requestTo("http://localhost:9090/users"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(json))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.createUser(userDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        server.verify();
    }

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        String jsonResponse = """
        [
            {"id":1,"name":"Алексей","email":"alex@example.com"},
            {"id":2,"name":"Мария","email":"maria@example.com"}
        ]
        """;

        server.expect(requestTo("http://localhost:9090/users"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        server.verify();
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        String jsonResponse = """
        {"id":1,"name":"Алексей","email":"alex@example.com"}
        """;

        server.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.getUserById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        server.verify();
    }

    @Test
    void updateUser_shouldSendPatchRequest() throws Exception {
        UserDto userDto = new UserDto(null, "Алексей обновлённый", "alex-upd@example.com");
        String jsonRequest = objectMapper.writeValueAsString(userDto);

        String jsonResponse = """
        {"id":1,"name":"Алексей обновлённый","email":"alex-upd@example.com"}
        """;

        server.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(content().json(jsonRequest))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.updateUser(1L, userDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        server.verify();
    }

    @Test
    void deleteUser_shouldSendDeleteRequest() {
        server.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        ResponseEntity<Object> response = userClient.deleteUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        server.verify();
    }
}