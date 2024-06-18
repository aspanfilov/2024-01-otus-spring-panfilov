package ru.otus.hw.services;

import lombok.Getter;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Getter
public class CacheService {

    private final Map<Long, MongoAuthor> authorsCache = new ConcurrentHashMap<>();

    private final Map<Long, MongoGenre> genresCache = new ConcurrentHashMap<>();

    private final Map<Long, MongoBook> booksCache = new ConcurrentHashMap<>();

    private final Map<Long, List<Long>> booksGenresCache = new ConcurrentHashMap<>();

}
