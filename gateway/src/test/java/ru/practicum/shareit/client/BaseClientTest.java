package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BaseClientTest {

    private RestTemplate restTemplate;
    private BaseClient baseClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        baseClient = new BaseClient(new DefaultUriBuilderFactory(""), restTemplate);
    }

    @Test
    void shouldSendGetRequestWithParams() {
        ResponseEntity<Object> mockResponse = ResponseEntity.ok("result");

        Map<String, Object> params = Map.of("from", 0, "size", 5);

        String expectedUrl = "/items?from=0&size=5";

        when(restTemplate.exchange(
                eq(expectedUrl),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(mockResponse);

        ResponseEntity<Object> response = baseClient.get("/items", 1L, params);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("result", response.getBody());
    }

    @Test
    void shouldSendPostRequestCorrectly() {
        ResponseEntity<Object> mockResponse = ResponseEntity.status(HttpStatus.CREATED).body("created");

        when(restTemplate.exchange(
                eq("/items"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(mockResponse);

        ResponseEntity<Object> response = baseClient.post("/items", 1L, "some body");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("created", response.getBody());
    }

    @Test
    void shouldSendDeleteRequest() {
        ResponseEntity<Object> mockResponse = ResponseEntity.ok("deleted");

        when(restTemplate.exchange(
                eq("/items/1"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(mockResponse);

        ResponseEntity<Object> response = baseClient.delete("/items/1", 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("deleted", response.getBody());
    }

    @Test
    void shouldSetCorrectHeaders() {
        ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), captor.capture(), eq(Object.class)))
                .thenReturn(ResponseEntity.ok("ok"));

        baseClient.get("/test", 123L);

        HttpHeaders headers = captor.getValue().getHeaders();
        assertEquals("123", headers.getFirst("X-Sharer-User-Id"));
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
    }

    @Test
    void shouldSendPatchRequestCorrectly() {
        ResponseEntity<Object> mockResponse = ResponseEntity.ok("patched");

        when(restTemplate.exchange(
                eq("/items/1"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenReturn(mockResponse);

        ResponseEntity<Object> response = baseClient.patch("/items/1", 1L, "patch-body");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("patched", response.getBody());
    }

    @Test
    void shouldHandleHttpClientErrorException() {
        HttpClientErrorException exception = new HttpClientErrorException(
                HttpStatus.BAD_REQUEST,
                "Bad Request",
                "Invalid data".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        when(restTemplate.exchange(
                eq("/items"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenThrow(exception);

        ResponseEntity<Object> response = baseClient.get("/items", 1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data", response.getBody());
    }

    @Test
    void shouldHandleHttpServerErrorException() {
        HttpServerErrorException exception = new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Error",
                "Something went wrong".getBytes(StandardCharsets.UTF_8),
                StandardCharsets.UTF_8
        );

        when(restTemplate.exchange(
                eq("/items"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class))
        ).thenThrow(exception);

        ResponseEntity<Object> response = baseClient.delete("/items", 1L);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Something went wrong", response.getBody());
    }
}