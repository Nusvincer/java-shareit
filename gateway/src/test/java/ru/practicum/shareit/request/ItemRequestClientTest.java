package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ItemRequestClientTest {

    private ItemRequestClient itemRequestClient;
    private MockRestServiceServer server;
    private RestTemplate testRestTemplate;

    private static final String BASE_URL = "http://localhost:9090/requests";
    private static final long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        testRestTemplate = new RestTemplate();
        testRestTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(BASE_URL));
        server = MockRestServiceServer.createServer(testRestTemplate);

        itemRequestClient = new ItemRequestClient(
                new DefaultUriBuilderFactory(BASE_URL),
                testRestTemplate
        );
    }

    @Test
    void createRequest_shouldReturnOkResponse() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Need a drill");

        server.expect(requestTo(BASE_URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(USER_ID)))
                .andRespond(withSuccess("{\"id\":1,\"description\":\"Need a drill\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemRequestClient.createRequest(USER_ID, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void getRequestById_shouldReturnRequest() {
        long requestId = 1L;

        server.expect(requestTo(BASE_URL + "/" + requestId))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(USER_ID)))
                .andRespond(withSuccess("{\"id\":1,\"description\":\"Need a drill\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemRequestClient.getRequestById(USER_ID, requestId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void getOwnRequests_shouldReturnList() {
        server.expect(requestTo(BASE_URL))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(USER_ID)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemRequestClient.getOwnRequests(USER_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void getRequestsOfOtherUsers_shouldReturnList() {
        server.expect(requestTo(BASE_URL + "/all"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(USER_ID)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemRequestClient.getRequestsOfOtherUsers(USER_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void getRequestsOfOtherUsers_withPagination_shouldReturnList() {
        int from = 0;
        int size = 10;

        server.expect(requestTo(containsString(BASE_URL + "/all")))
                .andExpect(requestTo(containsString("from=" + from)))
                .andExpect(requestTo(containsString("size=" + size)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(USER_ID)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemRequestClient.getRequestsOfOtherUsers(USER_ID, from, size);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }
}