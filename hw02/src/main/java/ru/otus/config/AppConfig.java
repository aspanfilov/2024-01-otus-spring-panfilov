package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.service.IO.IOService;
import ru.otus.service.IO.IOServiceStreams;

@Configuration
public class AppConfig {

    @Bean
    public IOService ioService() {
        return new IOServiceStreams(System.out);
    }
}
