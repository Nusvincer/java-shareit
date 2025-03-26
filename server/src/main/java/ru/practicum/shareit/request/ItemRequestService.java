package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(ItemRequestDto requestDto, Long userId);

    ItemRequestWithItemsDto getRequestById(Long requestId, Long userId);

    List<ItemRequestWithItemsDto> getAllRequests(Long userId);

    List<ItemRequestWithItemsDto> getRequestsOfOtherUsers(Long userId);
}