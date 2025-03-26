package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ItemRequestDto> createRequest(
            @RequestBody ItemRequestDto requestDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemRequestService.createRequest(requestDto, userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemRequestWithItemsDto> getRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id) {
        return ResponseEntity.ok(itemRequestService.getRequestById(id, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestWithItemsDto>> getOwnRequests(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemRequestService.getAllRequests(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestWithItemsDto>> getRequestsOfOtherUsers(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemRequestService.getRequestsOfOtherUsers(userId));
    }
}

