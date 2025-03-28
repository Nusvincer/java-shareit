package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.util.Map;

@Component
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${SHAREIT_SERVER_URL:http://localhost:9090}") String serverUrl) {
        super(new DefaultUriBuilderFactory(serverUrl + API_PREFIX));
    }

    public BookingClient(DefaultUriBuilderFactory factory, RestTemplate restTemplate) {
        super(factory, restTemplate);
    }

    public ResponseEntity<Object> createBooking(Long userId, BookingCreateDto bookingCreateDto) {
        return post("", userId, bookingCreateDto);
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> approveBooking(Long bookingId, boolean approved, Long ownerId) {
        return patch("/" + bookingId + "?approved=" + approved, ownerId, null);
    }

    public ResponseEntity<Object> getBookingsByUser(Long userId, String state) {
        return get("", userId, Map.of("state", state));
    }

    public ResponseEntity<Object> getBookingsForOwnerItems(Long ownerId, String state) {
        return get("/owner", ownerId, Map.of("state", state));
    }
}

