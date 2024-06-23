package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoBookComment;
import ru.otus.hw.models.mongo.MongoGenre;

@Service
@RequiredArgsConstructor
public class MongoDBCleanerService {

    private final MongoTemplate mongoTemplate;

    public void cleanMongoDB() {
        mongoTemplate.dropCollection(MongoAuthor.class);
        mongoTemplate.dropCollection(MongoGenre.class);
        mongoTemplate.dropCollection(MongoBook.class);
        mongoTemplate.dropCollection(MongoBookComment.class);
    }
}
