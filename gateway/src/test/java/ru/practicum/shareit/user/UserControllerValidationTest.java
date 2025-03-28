package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserClient userClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /users — должно вернуть 400, если email отсутствует")
    void createUser_shouldReturnBadRequest_whenEmailIsMissing() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users — должно вернуть 400, если email некорректный")
    void createUser_shouldReturnBadRequest_whenEmailInvalid() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("invalid-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users — должно вернуть 200 при валидных данных")
    void createUser_shouldReturnOk_whenValid() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("Valid User");
        userDto.setEmail("valid@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }
}