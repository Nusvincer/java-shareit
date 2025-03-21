/*package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository.deleteAll();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        UserDto userDto = new UserDto(null, "nusvincer", "nusvincer@gmail.com");
        User savedUser = new User(1L, "nusvincer", "nusvincer@gmail.com");

        when(userRepository.save(any())).thenReturn(savedUser);

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("nusvincer", result.getName());
        assertEquals("nusvincer@gmail.com", result.getEmail());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        User user = new User(1L, "nusvincer", "nusvincer@gmail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("nusvincer", result.getName());
        assertEquals("nusvincer@gmail.com", result.getEmail());

        verify(userRepository, times(1)).findById(1L);
    }
}*/