package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoBookComment;
import ru.otus.hw.models.sql.SqlBookComment;

@Service
@RequiredArgsConstructor
public class BookCommentProcessor implements ItemProcessor<SqlBookComment, MongoBookComment> {

    private final CacheService cacheService;

    @Override
    public MongoBookComment process(SqlBookComment sqlBookComment) {

        MongoBook mongoBook = cacheService.getBooksCache().get(sqlBookComment.getBookId());

        return MongoBookComment.builder()
                .id(new ObjectId().toString())
                .commentText(sqlBookComment.getCommentText())
                .book(mongoBook)
                .build();
    }
}
