package ru.otus.hw.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    public static final String GENRES_CACHE = "genres";

    public static final String ALL_GENRES_KEY = "'allGenres'";


    public static final String AUTHORS_CACHE = "authors";

    public static final String ALL_AUTHORS_KEY = "'allAuthors'";


    public static final String BOOKS_CACHE = "books";

    public static final String ALL_BOOKS_KEY = "'allBooks'";

    public static final String ENTITY_PREFIX = "'entity-'";


    public static final String BOOK_COMMENTS_CACHE = "bookComments";

    public static final String BOOK_ID_PREFIX = "'bookId-'";


    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                GENRES_CACHE,
                AUTHORS_CACHE,
                BOOKS_CACHE,
                BOOK_COMMENTS_CACHE);
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .maximumSize(10000)
        );
        return cacheManager;
    }
}
