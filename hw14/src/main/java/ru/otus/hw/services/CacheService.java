package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {

    private final Map<Long, MongoAuthor> authorsCache = new ConcurrentHashMap<>();

    private final Map<Long, MongoGenre> genresCache = new ConcurrentHashMap<>();

    private final Map<Long, MongoBook> booksCache = new ConcurrentHashMap<>();

    private final Map<Long, List<Long>> booksGenresCache = new ConcurrentHashMap<>();

    public void putAuthor(Long id, MongoAuthor author) {
        authorsCache.put(id, author);
    }

    public MongoAuthor getAuthor(Long id) {
        return authorsCache.get(id);
    }

    public void putGenre(Long id, MongoGenre genre) {
        genresCache.put(id, genre);
    }

    public MongoGenre getGenre(Long id) {
        return genresCache.get(id);
    }

    public void putBook(Long id, MongoBook book) {
        booksCache.put(id, book);
    }

    public MongoBook getBook(Long id) {
        return booksCache.get(id);
    }

    public void addGenreToBook(Long bookId, Long genreId) {
        booksGenresCache.computeIfAbsent(bookId, k -> new ArrayList<>()).add(genreId);
    }

    public List<Long> getBookGenres(Long bookId) {
        return booksGenresCache.get(bookId);
    }

    public boolean hasBookGenres(Long bookId) {
        return booksGenresCache.containsKey(bookId);
    }

}
