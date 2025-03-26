package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto getBookingById(Long id, Long userId);

    List<BookingDto> getAllBookings();

    BookingDto approveBooking(Long bookingId, boolean approved, Long ownerId);

    List<BookingDto> getBookingsByUser(Long userId, BookingState state);

    List<BookingDto> getBookingsForOwnerItems(Long ownerId, BookingState state);

    BookingDto createBooking(Long userId, BookingCreateDto bookingCreateDto);
}
