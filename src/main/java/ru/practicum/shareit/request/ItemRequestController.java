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
    public ResponseEntity<ItemRequestDto> createRequest(@RequestBody ItemRequestDto requestDto) {
        return ResponseEntity.ok(itemRequestService.createRequest(requestDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemRequestDto> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(itemRequestService.getRequestById(id));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getAllRequests() {
        return ResponseEntity.ok(itemRequestService.getAllRequests());
    }
}
