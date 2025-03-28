package ru.practicum.shareit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.booking.BookingClient;

@Configuration
public class ClientConfig {

    @Bean
    public ItemClient itemClient(@Value("${SHAREIT_SERVER_URL:http://localhost:9090}") String serverUrl) {
        return new ItemClient(serverUrl, new RestTemplate());
    }

    @Bean
    public ItemRequestClient itemRequestClient(@Value("${SHAREIT_SERVER_URL:http://localhost:9090}") String serverUrl) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(serverUrl + "/requests");
        return new ItemRequestClient(factory, new RestTemplate());
    }

    @Bean
    public BookingClient bookingClient(@Value("${SHAREIT_SERVER_URL:http://localhost:9090}") String serverUrl) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(serverUrl + "/bookings");
        return new BookingClient(factory, new RestTemplate());
    }
}