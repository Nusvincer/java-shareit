package ru.practicum.shareit;

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
    }

    @Test
    void createUser_ShouldReturnCreatedUser() {
        UserDto userDto = new UserDto(null, "nusvincer", "nusvincer@gmail.com");
        User savedUser = new User(1L, "nusvincer", "nusvincer@gmail.com");

        when(userRepository.save(any())).thenAnswer(invocation -> {
            User argument = invocation.getArgument(0);
            System.out.println("Сохранённый пользователь: " + argument);
            return savedUser;
        });

        UserDto result = userService.createUser(userDto);

        assertNotNull(result);
        assertEquals(1L, result.getId(), "ID не совпадает");
        assertEquals("nusvincer", result.getName(), "Имя не совпадает");
        assertEquals("nusvincer@gmail.com", result.getEmail(), "Email не совпадает");

        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getUserById_ShouldReturnUser() {
        User user = new User(1L, "nusvincer", "nusvincer@gmail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("nusvincer", result.getName(), "Имя не совпадает");
        assertEquals("nusvincer@gmail.com", result.getEmail(), "Email не совпадает");

        verify(userRepository, times(1)).findById(1L);
    }
}