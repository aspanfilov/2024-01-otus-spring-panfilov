package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.sql.SqlAuthor;

@Service
@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<SqlAuthor, MongoAuthor> {

    private final CacheService cacheService;

    @Override
    public MongoAuthor process(SqlAuthor sqlAuthor) {

        String id = new ObjectId().toString();
        MongoAuthor mongoAuthor = new MongoAuthor(id, sqlAuthor.getFullName());
        cacheService.getAuthorsCache().put(sqlAuthor.getId(), mongoAuthor);

        return mongoAuthor;
    }
}
