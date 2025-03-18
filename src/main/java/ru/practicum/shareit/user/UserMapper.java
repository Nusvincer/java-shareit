package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public class UserMapper {
    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toEntity(UserDto dto) {
        return new User(dto.getId(), dto.getEmail(), dto.getName());
    }
}
