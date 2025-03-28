package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.testutils.NoOpResponseErrorHandler;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class UserClientTest {

    private UserClient userClient;
    private MockRestServiceServer server;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        RestTemplate testRestTemplate = new RestTemplate();
        testRestTemplate.setErrorHandler(new NoOpResponseErrorHandler());

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
        server.verify();
    }

    @Test
    void getAllUsers_shouldReturnList() {
        String jsonResponse = "[{\"id\":1,\"name\":\"Алексей\",\"email\":\"alex@example.com\"}]";

        server.expect(requestTo("http://localhost:9090/users"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.getAllUsers();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void getUserById_shouldReturnUser() {
        String jsonResponse = "{\"id\":1,\"name\":\"Алексей\",\"email\":\"alex@example.com\"}";

        server.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.getUserById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void updateUser_shouldSendPatchRequest() throws Exception {
        UserDto userDto = new UserDto(null, "Алексей обновлённый", "alex-upd@example.com");
        String jsonRequest = objectMapper.writeValueAsString(userDto);

        String jsonResponse = "{\"id\":1,\"name\":\"Алексей обновлённый\",\"email\":\"alex-upd@example.com\"}";

        server.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(content().json(jsonRequest))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.updateUser(1L, userDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void updateUser_withEmptyFields_shouldReturnOkResponse() throws Exception {
        UserDto userDto = new UserDto(null, "", "");
        String jsonRequest = objectMapper.writeValueAsString(userDto);
        String jsonResponse = "{\"id\":1,\"name\":\"\",\"email\":\"\"}";

        server.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(content().json(jsonRequest))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = userClient.updateUser(1L, userDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void deleteUser_shouldSendDeleteRequest() {
        server.expect(requestTo("http://localhost:9090/users/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withSuccess());

        ResponseEntity<Object> response = userClient.deleteUser(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void getUserById_notFound_shouldReturn404() {
        server.expect(requestTo("http://localhost:9090/users/999"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        ResponseEntity<Object> response = userClient.getUserById(999L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        server.verify();
    }

    @Test
    void createUser_withInvalidEmail_shouldReturnBadRequest() throws Exception {
        UserDto userDto = new UserDto(null, "Имя", "invalid-email");
        String json = objectMapper.writeValueAsString(userDto);

        server.expect(requestTo("http://localhost:9090/users"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(json))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        ResponseEntity<Object> response = userClient.createUser(userDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        server.verify();
    }

    @Test
    void deleteUser_nonExistent_shouldReturnNotFound() {
        server.expect(requestTo("http://localhost:9090/users/999"))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        ResponseEntity<Object> response = userClient.deleteUser(999L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        server.verify();
    }

    @Test
    void getAllUsers_serverError_shouldReturn500() {
        server.expect(requestTo("http://localhost:9090/users"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseEntity<Object> response = userClient.getAllUsers();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        server.verify();
    }
}