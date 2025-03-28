package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemDtoJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void shouldSerializeAndDeserializeSimpleItemDto() throws Exception {
        ItemDto dto = new ItemDto(
                1L,
                "Дрель",
                "Простая дрель",
                true,
                10L,
                123L
        );

        String json = objectMapper.writeValueAsString(dto);
        ItemDto result = objectMapper.readValue(json, ItemDto.class);

        assertThat(result.getId()).isEqualTo(dto.getId());
        assertThat(result.getName()).isEqualTo(dto.getName());
        assertThat(result.getDescription()).isEqualTo(dto.getDescription());
        assertThat(result.getAvailable()).isEqualTo(dto.getAvailable());
        assertThat(result.getRequestId()).isEqualTo(dto.getRequestId());
        assertThat(result.getOwnerId()).isEqualTo(dto.getOwnerId());
    }

    @Test
    void shouldSerializeAndDeserializeItemDtoWithBookingsAndComments() throws Exception {
        LocalDateTime now = LocalDateTime.now();

        BookingShortDto lastBooking = new BookingShortDto(1L, 2L, now.minusDays(1), now);
        BookingShortDto nextBooking = new BookingShortDto(3L, 4L, now.plusDays(1), now.plusDays(2));
        CommentDto comment = new CommentDto(1L, "Класс!", "Петя", now);

        ItemDto dto = new ItemDto(1L, "Дрель", "Описание", true, 10L, 123L);
        dto.setLastBooking(lastBooking);
        dto.setNextBooking(nextBooking);
        dto.setComments(List.of(comment));

        String json = objectMapper.writeValueAsString(dto);
        ItemDto result = objectMapper.readValue(json, ItemDto.class);

        assertThat(result.getLastBooking().getId()).isEqualTo(1L);
        assertThat(result.getNextBooking().getId()).isEqualTo(3L);
        assertThat(result.getComments()).hasSize(1);
        assertThat(result.getComments().get(0).getText()).isEqualTo("Класс!");
    }
}