package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingClient bookingClient;

    @Test
    void shouldCreateBooking() throws Exception {
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setItemId(1L);
        dto.setStart(start);
        dto.setEnd(end);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBookingById() throws Exception {
        when(bookingClient.getBooking(1L, 1L)).thenReturn(ResponseEntity.ok("Booking 1"));

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Booking 1")));
    }

    @Test
    void shouldApproveBooking() throws Exception {
        when(bookingClient.approveBooking(1L, true, 1L))
                .thenReturn(ResponseEntity.ok("Booking approved"));

        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Booking approved")));
    }

    @Test
    void shouldGetBookingsByUser() throws Exception {
        when(bookingClient.getBookingsByUser(1L, "ALL"))
                .thenReturn(ResponseEntity.ok("Bookings for user"));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Bookings for user")));
    }

    @Test
    void shouldGetBookingsForOwnerItems() throws Exception {
        when(bookingClient.getBookingsForOwnerItems(1L, "ALL"))
                .thenReturn(ResponseEntity.ok("Bookings for owner items"));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Bookings for owner items")));
    }
}