package ru.otus.hw.config;

import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoBookComment;
import ru.otus.hw.models.mongo.MongoGenre;

@Configuration
public class WriterConfig {

    @Bean
    public MongoItemWriter<MongoAuthor> authorWriter(MongoTemplate mongoTemplate) {

        return new MongoItemWriterBuilder<MongoAuthor>()
                .template(mongoTemplate)
                .collection("authors")
                .build();
    }

    @Bean
    public MongoItemWriter<MongoGenre> genreWriter(MongoTemplate mongoTemplate) {

        return new MongoItemWriterBuilder<MongoGenre>()
                .template(mongoTemplate)
                .collection("genres")
                .build();
    }

    @Bean
    public MongoItemWriter<MongoBookComment> bookCommentWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<MongoBookComment>()
                .template(mongoTemplate)
                .collection("book_comments")
                .build();
    }

    @Bean
    public MongoItemWriter<MongoBook> bookWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<MongoBook>()
                .template(mongoTemplate)
                .collection("books")
                .build();
    }


}
