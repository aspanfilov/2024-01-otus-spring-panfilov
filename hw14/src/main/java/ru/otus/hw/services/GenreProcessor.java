package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.models.sql.SqlGenre;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenreProcessor implements ItemProcessor<SqlGenre, MongoGenre> {

    private final Map<Long, MongoGenre> genresCache;

    @Override
    public MongoGenre process(SqlGenre sqlGenre) {

        String id = new ObjectId().toString();
        MongoGenre mongoGenre = new MongoGenre(id, sqlGenre.getName());
        genresCache.put(sqlGenre.getId(), mongoGenre);

        return mongoGenre;
    }
}
