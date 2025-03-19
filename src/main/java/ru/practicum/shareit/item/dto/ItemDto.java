package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.practicum.shareit.user.dto.UserDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название предмета не должно быть пустым")
    private String name;
    private String description;
    private Boolean available;
    private UserDto owner;
}
