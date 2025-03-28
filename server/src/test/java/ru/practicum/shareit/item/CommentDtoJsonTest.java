package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentDtoJsonTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldSerializeAndDeserializeCommentDto() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        CommentDto dto = new CommentDto(1L, "Отличная вещь!", "Алексей", now);

        String json = objectMapper.writeValueAsString(dto);
        CommentDto result = objectMapper.readValue(json, CommentDto.class);

        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getText()).isEqualTo(dto.getText());
        assertThat(result.getAuthorName()).isEqualTo(dto.getAuthorName());
        assertThat(result.getCreated()).isEqualTo(dto.getCreated());
    }
}