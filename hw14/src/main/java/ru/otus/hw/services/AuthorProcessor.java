package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.sql.SqlAuthor;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthorProcessor implements ItemProcessor<SqlAuthor, MongoAuthor> {

    private final Map<Long, MongoAuthor> authorsCache;

    @Override
    public MongoAuthor process(SqlAuthor sqlAuthor) {

        String id = new ObjectId().toString();
        MongoAuthor mongoAuthor = new MongoAuthor(id, sqlAuthor.getFullName());
        authorsCache.put(sqlAuthor.getId(), mongoAuthor);

        return mongoAuthor;
    }
}
