package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.dto.ItemDto;

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
}