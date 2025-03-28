package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestDtoJsonTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeAndDeserializeItemRequestDto() throws Exception {
        LocalDateTime created = LocalDateTime.now();
        ItemRequestDto dto = new ItemRequestDto(1L, "Нужна дрель", 99L, created);

        String json = objectMapper.writeValueAsString(dto);
        ItemRequestDto result = objectMapper.readValue(json, ItemRequestDto.class);

        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getDescription()).isEqualTo(dto.getDescription());
        assertThat(result.getRequesterId()).isEqualTo(dto.getRequesterId());
        assertThat(result.getCreated()).isEqualTo(dto.getCreated());
    }
}