package ru.practicum.shareit.request;

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

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Test
    void shouldCreateItemRequest() throws Exception {
        String json = "{\"description\":\"Need a drill\"}";

        when(itemRequestClient.createRequest(Mockito.any(), Mockito.any()))
                .thenReturn(ResponseEntity.ok("Request created"));

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Request created")));
    }

    @Test
    void shouldReturnItemRequestById() throws Exception {
        when(itemRequestClient.getRequestById(1L, 1L)).thenReturn(ResponseEntity.ok("Request 1"));

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Request 1")));
    }

    @Test
    void shouldReturnOwnItemRequests() throws Exception {
        when(itemRequestClient.getOwnRequests(1L)).thenReturn(ResponseEntity.ok("Own requests"));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Own requests")));
    }

    @Test
    void shouldReturnItemRequestsOfOtherUsers() throws Exception {
        when(itemRequestClient.getRequestsOfOtherUsers(1L, 0, 10))
                .thenReturn(ResponseEntity.ok("Requests from other users"));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Requests from other users")));
    }

    @Test
    void shouldReturnItemRequestsOfOtherUsersWithPagination() throws Exception {
        // Мокаем поведение клиента
        when(itemRequestClient.getRequestsOfOtherUsers(1L, 0, 10))
                .thenReturn(ResponseEntity.ok("Requests from other users with pagination"));

        // Выполняем запрос к контроллеру
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())  // Проверка успешного ответа
                .andExpect(content().string("Requests from other users with pagination"));  // Проверка контента
    }
}