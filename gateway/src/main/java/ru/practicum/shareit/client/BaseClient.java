package ru.practicum.shareit.client;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

public class BaseClient {

    protected final RestTemplate rest;

    public BaseClient(DefaultUriBuilderFactory uriBuilderFactory) {
        this.rest = new RestTemplate();
        this.rest.setUriTemplateHandler(uriBuilderFactory);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        this.rest.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    public BaseClient(DefaultUriBuilderFactory uriBuilderFactory, RestTemplate restTemplate) {
        this.rest = restTemplate;
        this.rest.setUriTemplateHandler(uriBuilderFactory);
    }

    protected <T> ResponseEntity<Object> post(String path, long userId, T body) {
        try {
            return rest.exchange(
                    path,
                    HttpMethod.POST,
                    new HttpEntity<>(body, defaultHeaders(userId)),
                    Object.class
            );
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    protected <T> ResponseEntity<Object> patch(String path, long userId, T body) {
        try {
            return rest.exchange(
                    path,
                    HttpMethod.PATCH,
                    new HttpEntity<>(body, defaultHeaders(userId)),
                    Object.class
            );
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    protected ResponseEntity<Object> get(String path, long userId) {
        try {
            return rest.exchange(
                    path,
                    HttpMethod.GET,
                    new HttpEntity<>(defaultHeaders(userId)),
                    Object.class
            );
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    protected ResponseEntity<Object> get(String path, long userId, Map<String, Object> params) {
        try {
            return rest.exchange(
                    path + buildQueryString(params),
                    HttpMethod.GET,
                    new HttpEntity<>(defaultHeaders(userId)),
                    Object.class
            );
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    protected ResponseEntity<Object> delete(String path, long userId) {
        try {
            return rest.exchange(
                    path,
                    HttpMethod.DELETE,
                    new HttpEntity<>(defaultHeaders(userId)),
                    Object.class
            );
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    private String buildQueryString(Map<String, Object> params) {
        if (params.isEmpty()) {
            return "";
        }
        StringBuilder query = new StringBuilder("?");
        params.forEach((key, value) -> {
            if (query.length() > 1) {
                query.append("&");
            }
            query.append(key).append("=").append(value);
        });
        return query.toString();
    }

    private HttpHeaders defaultHeaders(long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Sharer-User-Id", String.valueOf(userId));
        return headers;
    }

    protected RestTemplate getRestTemplate() {
        return rest;
    }
}