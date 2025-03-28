package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingDtoJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void shouldSerializeAndDeserializeCorrectly() throws Exception {
        BookingCreateDto dto = new BookingCreateDto(
                1L,
                LocalDateTime.of(2025, 3, 28, 12, 0),
                LocalDateTime.of(2025, 3, 29, 12, 0)
        );

        String json = objectMapper.writeValueAsString(dto);
        BookingCreateDto result = objectMapper.readValue(json, BookingCreateDto.class);

        assertThat(result.getItemId()).isEqualTo(dto.getItemId());
        assertThat(result.getStart()).isEqualTo(dto.getStart());
        assertThat(result.getEnd()).isEqualTo(dto.getEnd());
    }
}