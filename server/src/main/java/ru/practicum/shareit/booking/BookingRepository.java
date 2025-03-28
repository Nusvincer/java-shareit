package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker(User booker);

    List<Booking> findByItem(Item item);

    List<Booking> findByBookerAndStartBeforeAndEndAfter(User booker, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerAndEndBefore(User booker, LocalDateTime end);

    List<Booking> findByBookerAndStartAfter(User booker, LocalDateTime start);

    List<Booking> findByBookerAndStatus(User booker, BookingStatus status);

    List<Booking> findByItemIn(List<Item> items);

    Optional<Booking> findFirstByItemIdAndStartBeforeOrderByStartDesc(Long itemId, LocalDateTime now);

    Optional<Booking> findFirstByItemIdAndStartAfterOrderByStartAsc(Long itemId, LocalDateTime now);

    List<Booking> findByBookerAndItemAndEndBefore(User booker, Item item, LocalDateTime end);
}

