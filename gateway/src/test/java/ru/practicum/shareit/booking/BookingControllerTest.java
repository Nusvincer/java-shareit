package ru.practicum.shareit.booking;

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

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @Test
    void shouldCreateBooking() throws Exception {
        String json = "{\"itemId\":1,\"start\":\"2025-03-28T10:00:00\",\"end\":\"2025-03-28T12:00:00\"}";

        when(bookingClient.createBooking(Mockito.any(), Mockito.any()))
                .thenReturn(ResponseEntity.ok("Booking created"));

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Booking created")));
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