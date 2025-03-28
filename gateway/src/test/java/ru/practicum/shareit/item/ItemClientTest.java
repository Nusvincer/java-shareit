package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ItemClientTest {

    private ItemClient itemClient;
    private MockRestServiceServer server;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        itemClient = new ItemClient("http://localhost:9090", restTemplate);
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void createItem_shouldReturnOkResponse() throws Exception {
        ItemDto itemDto = new ItemDto(null, "Дрель", "Простая дрель", true, null);
        String expectedJson = objectMapper.writeValueAsString(itemDto);

        server.expect(requestTo("http://localhost:9090/items"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(expectedJson))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andRespond(withSuccess(expectedJson, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.createItem(1L, itemDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        server.verify();
    }

    @Test
    void updateItem_shouldReturnOkResponse() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Новая дрель", "Обновленная дрель", true, null);
        String expectedJson = objectMapper.writeValueAsString(itemDto);

        server.expect(requestTo("http://localhost:9090/items/1"))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(content().json(expectedJson))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andRespond(withSuccess(expectedJson, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.updateItem(1L, 1L, itemDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void getItem_shouldReturnOkResponse() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Простая дрель", true, null);
        String expectedJson = objectMapper.writeValueAsString(itemDto);

        server.expect(requestTo("http://localhost:9090/items/1"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andRespond(withSuccess(expectedJson, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.getItem(1L, 1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String actualJson = objectMapper.writeValueAsString(response.getBody());
        assertThat(actualJson).isEqualTo(expectedJson);

        server.verify();
    }

    @Test
    void deleteItem_shouldReturnOkResponse() {
        server.expect(requestTo("http://localhost:9090/items/1"))
                .andExpect(method(HttpMethod.DELETE))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andRespond(withSuccess());

        ResponseEntity<Object> response = itemClient.deleteItem(1L, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void searchItems_shouldReturnOkResponse() {
        String encodedText = URLEncoder.encode("дрель", StandardCharsets.UTF_8);
        String expectedUri = "http://localhost:9090/items/search?text=" + encodedText;

        server.expect(requestTo(expectedUri))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.searchItems("дрель", 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void getUserItems_shouldReturnOkResponse() {
        server.expect(requestTo("http://localhost:9090/items"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.getUserItems(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void addComment_shouldReturnOkResponse() throws Exception {
        CommentDto commentDto = new CommentDto(null, "Отличная вещь!", null, null);
        String expectedJson = objectMapper.writeValueAsString(commentDto);

        server.expect(requestTo("http://localhost:9090/items/1/comment"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Sharer-User-Id", "1"))
                .andExpect(content().json(expectedJson))
                .andRespond(withSuccess(expectedJson, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.addComment(1L, 1L, commentDto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }
}