package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Item item = ItemMapper.toEntity(itemDto, owner);
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(Long id) {
        return itemRepository.findById(id)
                .map(ItemMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @Override
    public List<ItemDto> getAllItems() {
        return itemRepository.findAllWithOwners().stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        List<Item> items = itemRepository
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text);

        for (Item item : items) {
            System.out.println("Item: " + item.getId() + ", Available: " + item.getAvailable());
        }

        return items.stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto, Long ownerId) {
        if (ownerId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ID is required");
        }

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));

        if (!item.getOwner().getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner of this item");
        }

        Optional.ofNullable(itemDto.getName()).ifPresent(item::setName);
        Optional.ofNullable(itemDto.getDescription()).ifPresent(item::setDescription);
        Optional.ofNullable(itemDto.getAvailable()).ifPresent(item::setAvailable);

        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
