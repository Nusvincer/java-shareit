package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        ItemRequest request = null;
        if (itemDto.getRequestId() != null) {
            request = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        }

        Item item = ItemMapper.toEntity(itemDto, owner, request);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long id, Long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        ItemDto itemDto = ItemMapper.toDto(item);

        if (item.getOwner().getId().equals(userId)) {
            bookingRepository.findFirstByItemIdAndStartBeforeOrderByStartDesc(
                            item.getId(), LocalDateTime.now())
                    .ifPresent(booking -> itemDto.setLastBooking(
                            new BookingShortDto(booking.getId(), booking.getBooker().getId(),
                                    booking.getStart(), booking.getEnd())
                    ));

            bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(
                            item.getId(), LocalDateTime.now())
                    .ifPresent(booking -> itemDto.setNextBooking(
                            new BookingShortDto(booking.getId(), booking.getBooker().getId(),
                                    booking.getStart(), booking.getEnd())
                    ));
        }

        List<CommentDto> comments = commentRepository.findByItemId(item.getId())
                .stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
        itemDto.setComments(comments);

        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        return itemRepository.searchAvailableItems(text)
                .stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto, Long ownerId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this item");
        }

        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());

        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        List<Item> items = itemRepository.findByOwnerId(ownerId);

        return items.stream()
                .map(item -> {
                    ItemDto itemDto = ItemMapper.toDto(item);

                    bookingRepository.findFirstByItemIdAndStartBeforeOrderByStartDesc(
                                    item.getId(), LocalDateTime.now())
                            .ifPresent(booking -> itemDto.setLastBooking(
                                    new BookingShortDto(booking.getId(), booking.getBooker().getId(),
                                            booking.getStart(), booking.getEnd())
                            ));

                    bookingRepository.findFirstByItemIdAndStartAfterOrderByStartAsc(
                                    item.getId(), LocalDateTime.now())
                            .ifPresent(booking -> itemDto.setNextBooking(
                                    new BookingShortDto(booking.getId(), booking.getBooker().getId(),
                                            booking.getStart(), booking.getEnd())
                            ));

                    List<CommentDto> comments = commentRepository.findByItemId(item.getId())
                            .stream()
                            .map(CommentMapper::toDto)
                            .collect(Collectors.toList());
                    itemDto.setComments(comments);

                    return itemDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Long itemId, Long authorId, String text) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean hasBooking = bookingRepository
                .findByBookerAndItemAndEndBefore(author, item, LocalDateTime.now())
                .stream()
                .anyMatch(booking -> booking.getStatus() == ru.practicum.shareit.booking.BookingStatus.APPROVED);

        if (!hasBooking) {
            throw new ValidationException("User has not completed booking of this item");
        }

        Comment comment = new Comment();
        comment.setText(text);
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toDto(commentRepository.save(comment));
    }
}

