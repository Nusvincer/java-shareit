package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto createBooking(Long userId, BookingCreateDto bookingCreateDto) {
        if (bookingCreateDto.getStart() == null || bookingCreateDto.getEnd() == null) {
            throw new ValidationException("Start and end dates must be provided.");
        }

        if (bookingCreateDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Start date cannot be in the past.");
        }

        if (bookingCreateDto.getEnd().isBefore(bookingCreateDto.getStart()) ||
                bookingCreateDto.getEnd().isEqual(bookingCreateDto.getStart())) {
            throw new ValidationException("End date must be after start date.");
        }

        Item item = itemRepository.findById(bookingCreateDto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (!item.getAvailable()) {
            throw new ValidationException("Item is not available for booking");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Owner cannot book their own item.");
        }

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(bookingCreateDto.getStart());
        booking.setEnd(bookingCreateDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);

        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(BookingMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
    }

    @Override
    public List<BookingDto> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto approveBooking(Long bookingId, boolean approved, Long ownerId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        Item item = booking.getItem();
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the owner can approve or reject the booking.");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalStateException("Booking already approved or rejected.");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDto> getBookingsByUser(Long userId, BookingState state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Booking> bookings;

        switch (state) {
            case CURRENT -> bookings = bookingRepository.findByBookerAndStartBeforeAndEndAfter(user, LocalDateTime.now(), LocalDateTime.now());
            case PAST -> bookings = bookingRepository.findByBookerAndEndBefore(user, LocalDateTime.now());
            case FUTURE -> bookings = bookingRepository.findByBookerAndStartAfter(user, LocalDateTime.now());
            case WAITING -> bookings = bookingRepository.findByBookerAndStatus(user, BookingStatus.WAITING);
            case REJECTED -> bookings = bookingRepository.findByBookerAndStatus(user, BookingStatus.REJECTED);
            default -> bookings = bookingRepository.findByBooker(user);
        }

        return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsForOwnerItems(Long ownerId, BookingState state) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<Item> ownerItems = itemRepository.findByOwnerId(ownerId);
        List<Booking> bookings = bookingRepository.findByItemIn(ownerItems);

        LocalDateTime now = LocalDateTime.now();

        return bookings.stream()
                .filter(booking -> switch (state) {
                    case CURRENT -> booking.getStart().isBefore(now) && booking.getEnd().isAfter(now);
                    case PAST -> booking.getEnd().isBefore(now);
                    case FUTURE -> booking.getStart().isAfter(now);
                    case WAITING -> booking.getStatus() == BookingStatus.WAITING;
                    case REJECTED -> booking.getStatus() == BookingStatus.REJECTED;
                    default -> true;
                })
                .map(BookingMapper::toDto)
                .collect(Collectors.toList());
    }
}