package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AppConfig {

    @Bean
    public Map<Long, List<Long>> booksGenresCache() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, MongoBook> booksCache() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, MongoAuthor> authorsCache() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<Long, MongoGenre> genresCache() {
        return new ConcurrentHashMap<>();
    }

}
