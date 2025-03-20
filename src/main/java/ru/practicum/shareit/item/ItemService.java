package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto getItemById(Long id);

    List<ItemDto> getAllItems();

    List<ItemDto> searchItems(String text);

    ItemDto updateItem(Long id, ItemDto itemDto, Long ownerId);

    void deleteItem(Long id);

    List<ItemDto> getItemsByOwner(Long ownerId);
}
