package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequester() != null ? request.getRequester().getId() : null,
                request.getCreated()
        );
    }

    public static ItemRequestWithItemsDto toDtoWithItems(ItemRequest request, List<Item> items) {
        List<ItemDto> itemDtos = items.stream()
                .map(item -> new ItemDto(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getAvailable(),
                        item.getOwner() != null ? item.getOwner().getId() : null,
                        item.getRequest() != null ? item.getRequest().getId() : null
                ))
                .collect(Collectors.toList());

        return new ItemRequestWithItemsDto(
                request.getId(),
                request.getDescription(),
                request.getRequester() != null ? request.getRequester().getId() : null,
                request.getCreated(),
                itemDtos
        );
    }

    public static ItemRequest toEntity(ItemRequestDto dto) {
        ItemRequest request = new ItemRequest();
        request.setId(dto.getId());
        request.setDescription(dto.getDescription());
        request.setCreated(dto.getCreated());
        return request;
    }
}
