package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Map;

public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${SHAREIT_SERVER_URL:http://localhost:9090}") String serverUrl) {
        super(new DefaultUriBuilderFactory(serverUrl + API_PREFIX));
    }

    public ItemRequestClient(DefaultUriBuilderFactory uriBuilderFactory, RestTemplate restTemplate) {
        super(uriBuilderFactory, restTemplate);
    }

    public ResponseEntity<Object> createRequest(Long userId, ItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> getRequestById(Long userId, Long requestId) {
        return get("/" + requestId, userId);
    }

    public ResponseEntity<Object> getOwnRequests(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getRequestsOfOtherUsers(Long userId) {
        return get("/all", userId);
    }

    public ResponseEntity<Object> getRequestsOfOtherUsers(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all", userId, parameters);
    }
}