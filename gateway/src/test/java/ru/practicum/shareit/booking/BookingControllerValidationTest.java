package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /bookings — 400, если поля отсутствуют")
    void createBooking_shouldReturnBadRequest_whenFieldsMissing() throws Exception {
        BookingCreateDto booking = new BookingCreateDto();

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /bookings — 400, если даты не в будущем")
    void createBooking_shouldReturnBadRequest_whenDatesNotFuture() throws Exception {
        BookingCreateDto booking = new BookingCreateDto(
                1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().minusHours(1)
        );

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /bookings — 200 при валидных данных")
    void createBooking_shouldReturnOk_whenValid() throws Exception {
        BookingCreateDto booking = new BookingCreateDto(
                1L,
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2)
        );

        Mockito.when(bookingClient.createBooking(Mockito.anyLong(), Mockito.any()))
                .thenReturn(ResponseEntity.ok("Created"));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk());
    }
}