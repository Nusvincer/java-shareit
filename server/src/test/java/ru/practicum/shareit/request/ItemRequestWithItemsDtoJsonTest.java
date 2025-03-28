package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestWithItemsDtoJsonTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeAndDeserializeItemRequestWithItemsDto() throws Exception {
        LocalDateTime created = LocalDateTime.now();

        ItemDto item = new ItemDto(1L, "Отвертка", "Крестовая", true, 10L, 123L);
        ItemRequestWithItemsDto dto = new ItemRequestWithItemsDto(
                1L, "Нужна отвертка", 99L, created, List.of(item)
        );

        String json = objectMapper.writeValueAsString(dto);
        ItemRequestWithItemsDto result = objectMapper.readValue(json, ItemRequestWithItemsDto.class);

        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getDescription()).isEqualTo(dto.getDescription());
        assertThat(result.getRequesterId()).isEqualTo(dto.getRequesterId());
        assertThat(result.getCreated()).isEqualTo(dto.getCreated());
        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getName()).isEqualTo("Отвертка");
    }
}