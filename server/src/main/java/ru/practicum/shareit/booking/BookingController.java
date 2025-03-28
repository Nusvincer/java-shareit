package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
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
    public BookingDto createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody BookingCreateDto bookingCreateDto
    ) {
        return bookingService.createBooking(userId, bookingCreateDto);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(
            @PathVariable Long id,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        return bookingService.getBookingById(id, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(
            @PathVariable Long bookingId,
            @RequestParam boolean approved,
            @RequestHeader("X-Sharer-User-Id") Long ownerId
    ) {
        return bookingService.approveBooking(bookingId, approved, ownerId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByUser(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText
    ) {
        BookingState state = BookingState.valueOf(stateText.toUpperCase());
        return bookingService.getBookingsByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsForOwnerItems(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateText
    ) {
        BookingState state = BookingState.valueOf(stateText.toUpperCase());
        return bookingService.getBookingsForOwnerItems(ownerId, state);
    }
}

