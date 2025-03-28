package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemClient itemClient;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldCreateItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Item Description");
        itemDto.setAvailable(true);

        when(itemClient.createItem(1L, itemDto)).thenReturn(ResponseEntity.ok("Item created"));

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Item created")));
    }

    @Test
    void shouldGetItemById() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Item Description");
        itemDto.setAvailable(true);

        when(itemClient.getItem(1L, 1L)).thenReturn(ResponseEntity.ok(itemDto));

        mockMvc.perform(get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.description").value("Test Item Description"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void shouldSearchItems() throws Exception {
        String searchText = "drill";
        when(itemClient.searchItems(searchText, 1L)).thenReturn(ResponseEntity.ok("Items found"));

        mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", "1")
                        .param("text", searchText))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Items found")));
    }

    @Test
    void shouldUpdateItem() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Updated Item");
        itemDto.setDescription("Updated Item Description");
        itemDto.setAvailable(true);

        when(itemClient.updateItem(1L, 1L, itemDto)).thenReturn(ResponseEntity.ok("Item updated"));

        mockMvc.perform(patch("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Item updated")));
    }

    @Test
    void shouldDeleteItem() throws Exception {
        when(itemClient.deleteItem(1L, 1L)).thenReturn(ResponseEntity.ok("Item deleted"));

        mockMvc.perform(delete("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Item deleted")));
    }

    @Test
    void shouldAddCommentToItem() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setText("Great item!");

        when(itemClient.addComment(1L, 1L, commentDto)).thenReturn(ResponseEntity.ok("Comment added"));

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Comment added")));
    }
}