package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid BookingCreateDto bookingCreateDto
    ) {
        return bookingClient.createBooking(userId, bookingCreateDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getBookingById(
            @PathVariable Long id,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return bookingClient.getBooking(userId, id);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(
            @PathVariable Long bookingId,
            @RequestParam boolean approved,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return bookingClient.approveBooking(bookingId, approved, ownerId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUser(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText
    ) {
        return bookingClient.getBookingsByUser(userId, stateText);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsForOwnerItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText
    ) {
        return bookingClient.getBookingsForOwnerItems(ownerId, stateText);
    }
}

