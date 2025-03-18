package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(ItemRequestDto requestDto);
    ItemRequestDto getRequestById(Long id);
    List<ItemRequestDto> getAllRequests();
}
