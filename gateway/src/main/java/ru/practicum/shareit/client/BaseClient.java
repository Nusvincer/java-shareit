package ru.practicum.shareit.client;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

public class BaseClient {

    protected final RestTemplate rest;

    public BaseClient(DefaultUriBuilderFactory uriBuilderFactory) {
        this.rest = new RestTemplate();
        this.rest.setUriTemplateHandler(uriBuilderFactory);
    }

    protected <T> ResponseEntity<Object> post(String path, long userId, T body) {
        return rest.exchange(
                path,
                HttpMethod.POST,
                new HttpEntity<>(body, defaultHeaders(userId)),
                Object.class
        );
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId, T body) {
        return rest.exchange(
                path,
                HttpMethod.PATCH,
                new HttpEntity<>(body, defaultHeaders(userId)),
                Object.class
        );
    }

    protected ResponseEntity<Object> get(String path, long userId) {
        return rest.exchange(
                path,
                HttpMethod.GET,
                new HttpEntity<>(defaultHeaders(userId)),
                Object.class
        );
    }

    protected ResponseEntity<Object> get(String path, long userId, Map<String, Object> params) {
        return rest.exchange(
                path,
                HttpMethod.GET,
                new HttpEntity<>(defaultHeaders(userId)),
                Object.class,
                params
        );
    }

    protected ResponseEntity<Object> delete(String path, long userId) {
        return rest.exchange(
                path,
                HttpMethod.DELETE,
                new HttpEntity<>(defaultHeaders(userId)),
                Object.class
        );
    }

    private HttpHeaders defaultHeaders(long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Sharer-User-Id", String.valueOf(userId));
        return headers;
    }
}

