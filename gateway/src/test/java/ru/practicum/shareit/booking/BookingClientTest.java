package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class BookingClientTest {

    private BookingClient bookingClient;
    private MockRestServiceServer server;
    private RestTemplate testRestTemplate;

    private static final String BASE_URL = "http://localhost:9090/bookings";
    private static final long USER_ID = 1L;

    @BeforeEach
    void setUp() {
        testRestTemplate = new RestTemplate();
        testRestTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(BASE_URL));
        server = MockRestServiceServer.createServer(testRestTemplate);

        bookingClient = new BookingClient(
                new DefaultUriBuilderFactory(BASE_URL),
                testRestTemplate
        );
    }

    @Test
    void createBooking_shouldReturnOk() {
        BookingCreateDto dto = new BookingCreateDto();
        server.expect(requestTo(BASE_URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(USER_ID)))
                .andRespond(withSuccess("{\"id\":1}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.createBooking(USER_ID, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void getBooking_shouldReturnBooking() {
        long bookingId = 42L;
        server.expect(requestTo(BASE_URL + "/" + bookingId))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(USER_ID)))
                .andRespond(withSuccess("{\"id\":42}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.getBooking(USER_ID, bookingId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void approveBooking_shouldReturnOk() {
        long bookingId = 42L;
        boolean approved = true;

        server.expect(requestTo(BASE_URL + "/" + bookingId + "?approved=" + approved))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(USER_ID)))
                .andRespond(withSuccess("{\"status\":\"APPROVED\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.approveBooking(bookingId, approved, USER_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void getBookingsByUser_shouldReturnList() {
        String state = "ALL";

        server.expect(requestTo(BASE_URL + "?state=" + state))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(USER_ID)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.getBookingsByUser(USER_ID, state);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void getBookingsForOwnerItems_shouldReturnList() {
        String state = "ALL";

        server.expect(requestTo(BASE_URL + "/owner?state=" + state))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", String.valueOf(USER_ID)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = bookingClient.getBookingsForOwnerItems(USER_ID, state);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }
}