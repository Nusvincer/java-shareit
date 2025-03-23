package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody BookingCreateDto bookingCreateDto
    ) {
        return ResponseEntity.ok(bookingService.createBooking(userId, bookingCreateDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approveBooking(
            @PathVariable Long bookingId,
            @RequestParam boolean approved,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return ResponseEntity.ok(bookingService.approveBooking(bookingId, approved, ownerId));
    }

    @GetMapping
    public ResponseEntity<List<BookingDto>> getBookingsByUser(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText
    ) {
        BookingState state = BookingState.valueOf(stateText.toUpperCase());
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getBookingsForOwnerItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText
    ) {
        BookingState state = BookingState.valueOf(stateText.toUpperCase());
        return ResponseEntity.ok(bookingService.getBookingsForOwnerItems(ownerId, state));
    }
}
