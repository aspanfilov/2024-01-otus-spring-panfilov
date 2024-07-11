package ru.otus.hw.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import java.net.URISyntaxException;

@Configuration
@EnableCaching()
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
    public JCacheCacheManager cacheManager() throws URISyntaxException {

        var cacheManager = Caching.getCachingProvider().getCacheManager(
                getClass().getResource("/ehcache.xml").toURI(),
                getClass().getClassLoader());

        var jCacheCacheManager = new JCacheCacheManager(cacheManager);
        jCacheCacheManager.setTransactionAware(true);
        return jCacheCacheManager;
    }
}
