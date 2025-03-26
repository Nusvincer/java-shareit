package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto createRequest(ItemRequestDto requestDto, Long userId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        ItemRequest request = new ItemRequest();
        request.setDescription(requestDto.getDescription());
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());

        return ItemRequestMapper.toDto(itemRequestRepository.save(request));
    }

    @Override
    public ItemRequestWithItemsDto getRequestById(Long requestId, Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        List<Item> items = itemRepository.findAllByRequestId(requestId);
        return ItemRequestMapper.toDtoWithItems(request, items);
    }

    @Override
    public List<ItemRequestWithItemsDto> getAllRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(userId).stream()
                .map(request -> {
                    List<Item> items = itemRepository.findAllByRequestId(request.getId());
                    return ItemRequestMapper.toDtoWithItems(request, items);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestWithItemsDto> getRequestsOfOtherUsers(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        return itemRequestRepository.findAll().stream()
                .filter(request -> !request.getRequester().getId().equals(userId))
                .map(request -> {
                    List<Item> items = itemRepository.findAllByRequestId(request.getId());
                    return ItemRequestMapper.toDtoWithItems(request, items);
                })
                .collect(Collectors.toList());
    }
}