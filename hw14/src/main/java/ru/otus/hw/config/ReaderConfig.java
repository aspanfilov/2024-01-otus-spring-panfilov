package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.models.sql.SqlAuthor;
import ru.otus.hw.models.sql.SqlBook;
import ru.otus.hw.models.sql.SqlBookComment;
import ru.otus.hw.models.sql.SqlGenre;

@Configuration
public class ReaderConfig {

    @Autowired
    private AppProps appProps;

    @Bean
    public JpaPagingItemReader<SqlAuthor> authorReader(EntityManagerFactory entityManagerFactory) {

        return new JpaPagingItemReaderBuilder<SqlAuthor>()
                .name("authorReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from SqlAuthor a")
                .pageSize(appProps.getChunkSize())
                .build();
    }

    @Bean
    public JpaPagingItemReader<SqlGenre> genreReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<SqlGenre>()
                .name("genreReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select g from SqlGenre g")
                .pageSize(appProps.getChunkSize())
                .build();
    }

    @Bean
    public JpaPagingItemReader<SqlBook> bookReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<SqlBook>()
                .name("bookReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from SqlBook b")
                .pageSize(appProps.getChunkSize())
                .build();
    }

    @Bean
    public JpaPagingItemReader<SqlBookComment> bookCommentReader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<SqlBookComment>()
                .name("bookCommentReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select bc from SqlBookComment bc")
                .pageSize(appProps.getChunkSize())
                .build();
    }

}
