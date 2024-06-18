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

@Service
@RequiredArgsConstructor
public class BookProcessor implements ItemProcessor<SqlBook, MongoBook> {

    private final CacheService cacheService;

    @Override
    public MongoBook process(SqlBook book) {
        MongoAuthor mongoAuthor = cacheService.getAuthor(book.getAuthorId());

        List<MongoGenre> mongoGenres = new ArrayList<>();
        if (cacheService.hasBookGenres(book.getId())) {
            mongoGenres = cacheService.getBookGenres(book.getId()).stream()
                    .map(cacheService::getGenre).toList();
        }

        MongoBook mongoBook = MongoBook.builder()
                .id(new ObjectId().toString())
                .title(book.getTitle())
                .author(mongoAuthor)
                .genres(mongoGenres)
                .build();

        cacheService.putBook(book.getId(), mongoBook);

        return mongoBook;
    }

}
