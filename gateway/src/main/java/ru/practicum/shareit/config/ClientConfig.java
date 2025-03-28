package ru.practicum.shareit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.item.ItemClient;

@Configuration
public class ClientConfig {

    @Bean
    public ItemClient itemClient(@Value("${SHAREIT_SERVER_URL:http://localhost:9090}") String serverUrl) {
        return new ItemClient(serverUrl, new RestTemplate());
    }
}