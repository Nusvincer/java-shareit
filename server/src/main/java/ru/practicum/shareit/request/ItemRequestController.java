package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

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
    public ResponseEntity<ItemRequestDto> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(itemRequestService.getRequestById(id));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getOwnRequests() {
        return ResponseEntity.ok(itemRequestService.getAllRequests());
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getRequestsOfOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return ResponseEntity.ok(itemRequestService.getRequestsOfOtherUsers(userId));
    }
}

