/*package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemRepository.deleteAll();
    }

    @Test
    void createItem_ShouldReturnCreatedItem() {
        Long ownerId = 1L;
        User owner = new User(ownerId, "OwnerName", "owner@example.com");
        ItemDto itemDto = new ItemDto(null, "Дрель", "Мощная дрель", true, new UserDto(ownerId, "OwnerName", "owner@example.com"));
        Item savedItem = new Item(1L, "Дрель", "Мощная дрель", true, owner);

        when(userRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        ItemDto result = itemService.createItem(itemDto, ownerId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Дрель", result.getName());
        assertEquals("Мощная дрель", result.getDescription());
        assertTrue(result.getAvailable());
        assertEquals(ownerId, result.getOwner().getId());

        verify(userRepository, times(1)).findById(ownerId);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void getItemById_ShouldReturnItem() {
        Long itemId = 1L;
        Long ownerId = 1L;
        User owner = new User(ownerId, "OwnerName", "owner@example.com");
        Item item = new Item(itemId, "Дрель", "Мощная дрель", true, owner);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ItemDto result = itemService.getItemById(itemId);

        assertNotNull(result);
        assertEquals(itemId, result.getId());
        assertEquals("Дрель", result.getName());
        assertEquals("Мощная дрель", result.getDescription());
        assertTrue(result.getAvailable());
        assertEquals(ownerId, result.getOwner().getId());

        verify(itemRepository, times(1)).findById(itemId);
    }
}*/