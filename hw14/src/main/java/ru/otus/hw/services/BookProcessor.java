package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.models.sql.SqlBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<SqlBook, MongoBook> {

    private final Map<Long, MongoAuthor> authorsCache;

    private final Map<Long, MongoGenre> genresCache;

    private final Map<Long, List<Long>> booksGenresCache;

    private final Map<Long, MongoBook> booksCache;

    @Override
    public MongoBook process(SqlBook book) {
        MongoAuthor mongoAuthor = authorsCache.get(book.getAuthorId());

        List<MongoGenre> mongoGenres = new ArrayList<>();
        if (booksGenresCache.containsKey(book.getId())) {
            mongoGenres = booksGenresCache.get(book.getId()).stream()
                    .map(genresCache::get).toList();
        }

        MongoBook mongoBook = MongoBook.builder()
                .id(new ObjectId().toString())
                .title(book.getTitle())
                .author(mongoAuthor)
                .genres(mongoGenres)
                .build();

        booksCache.put(book.getId(), mongoBook);

        return mongoBook;
    }

}
