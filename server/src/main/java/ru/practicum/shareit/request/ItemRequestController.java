package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createRequest(
            @RequestBody ItemRequestDto requestDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.createRequest(requestDto, userId);
    }

    @GetMapping("/{id}")
    public ItemRequestWithItemsDto getRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id) {
        return itemRequestService.getRequestById(id, userId);
    }

    @GetMapping
    public List<ItemRequestWithItemsDto> getOwnRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestWithItemsDto> getRequestsOfOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getRequestsOfOtherUsers(userId);
    }
}

