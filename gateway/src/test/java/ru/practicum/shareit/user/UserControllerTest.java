package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserClient userClient;

    @Test
    void shouldCreateUser() throws Exception {
        String json = "{\"name\":\"Alex\",\"email\":\"alex@example.com\"}";

        when(userClient.createUser(Mockito.any()))
                .thenReturn(ResponseEntity.ok("Created"));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Created")));
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        when(userClient.getAllUsers()).thenReturn(ResponseEntity.ok("All users"));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("All users")));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        when(userClient.getUserById(1L)).thenReturn(ResponseEntity.ok("User 1"));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User 1")));
    }

    @Test
    void shouldReturnNotFoundForMissingUser() throws Exception {
        when(userClient.getUserById(99L)).thenReturn(ResponseEntity.status(404).body("Not found"));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Not found")));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        String json = "{\"name\":\"Updated\",\"email\":\"upd@example.com\"}";

        when(userClient.updateUser(Mockito.eq(1L), Mockito.any()))
                .thenReturn(ResponseEntity.ok("Updated"));

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Updated")));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        when(userClient.deleteUser(1L)).thenReturn(ResponseEntity.ok("Deleted"));

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Deleted")));
    }
}
