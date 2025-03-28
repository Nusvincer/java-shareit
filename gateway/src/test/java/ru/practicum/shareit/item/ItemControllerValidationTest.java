package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemClient itemClient;

    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        ItemDto invalidDto = new ItemDto();
        invalidDto.setDescription("desc");
        invalidDto.setAvailable(true);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(itemClient);
    }

    @Test
    void shouldReturn400WhenUserIdHeaderIsMissing() throws Exception {
        ItemDto dto = new ItemDto();
        dto.setName("Дрель");
        dto.setDescription("desc");
        dto.setAvailable(true);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(itemClient);
    }

    @Test
    void shouldReturn400WhenAvailableIsMissing() throws Exception {
        ItemDto dto = new ItemDto();
        dto.setName("Лопата");
        dto.setDescription("для снега");

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(itemClient);
    }

    @Test
    void shouldReturn400WhenDescriptionTooLong() throws Exception {
        String longDesc = "x".repeat(1000);
        ItemDto dto = new ItemDto();
        dto.setName("Дрель");
        dto.setDescription(longDesc);
        dto.setAvailable(true);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(itemClient);
    }

    @Test
    void shouldReturn400WhenPatchRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(itemClient);
    }

    @Test
    void shouldReturn400WhenUserIdHeaderIsMissingOnPatch() throws Exception {
        ItemDto dto = new ItemDto();
        dto.setName("Молоток");
        dto.setDescription("Тяжелый");
        dto.setAvailable(true);

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(itemClient);
    }
}
