package ru.otus.hw.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.models.sql.SqlAuthor;
import ru.otus.hw.models.sql.SqlBook;
import ru.otus.hw.models.sql.SqlGenre;
import ru.otus.hw.services.ProgressLoggerChunkListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class JobConfig {

    private static final int CHUNK_SIZE = 2;

    public static final String LIBRARY_MIGRATION_JOB_NAME = "libraryMigrationJob";

    private final Logger logger = LoggerFactory.getLogger("Batch");

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private EntityManager entityManager;

    private final Map<Long, MongoAuthor> authorCache = new ConcurrentHashMap<>();

    private final Map<Long, MongoGenre> genreCache = new ConcurrentHashMap<>();

    private final Map<Long, MongoBook> bookCache = new ConcurrentHashMap<>();


    @Bean
    public JpaCursorItemReader<SqlAuthor> authorReader(EntityManagerFactory entityManagerFactory) {

        return new JpaCursorItemReaderBuilder<SqlAuthor>()
                .name("authorReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from SqlAuthor a")
                .build();
    }

    @Bean
    public MongoItemWriter<MongoAuthor> authorWriter(MongoTemplate mongoTemplate) {

        return new MongoItemWriterBuilder<MongoAuthor>()
                .template(mongoTemplate)
                .collection("authors")
                .build();
    }

    @Bean
    public JpaCursorItemReader<SqlGenre> genreReader(EntityManagerFactory entityManagerFactory) {

        return new JpaCursorItemReaderBuilder<SqlGenre>()
                .name("genreReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select g from SqlGenre g")
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
    public JpaCursorItemReader<SqlBook> bookReader(EntityManagerFactory entityManagerFactory) {
        return new JpaCursorItemReaderBuilder<SqlBook>()
                .name("bookReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from SqlBook b")
                .build();
    }

    @Bean
    public MongoItemWriter<MongoBook> bookWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<MongoBook>()
                .template(mongoTemplate)
                .collection("books")
                .build();
    }

    @Bean
    public Step migrateAuthorsStep(JpaCursorItemReader<SqlAuthor> authorReader,
                                   MongoItemWriter<MongoAuthor> authorWriter) {

        int totalAuthors = ((Number) entityManager
                .createQuery("SELECT COUNT(a) FROM SqlAuthor a")
                .getSingleResult()).intValue();
        ProgressLoggerChunkListener<MongoAuthor> progressLogger = new ProgressLoggerChunkListener<>(totalAuthors);

        return new StepBuilder("migrateAuthorsStep", jobRepository)
                .<SqlAuthor, MongoAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(authorReader)
                .processor(author -> {
                    MongoAuthor mongoAuthor = new MongoAuthor(null, author.getFullName());
                    authorCache.put(author.getId(), mongoAuthor);
                    return mongoAuthor;
                })
                .writer(authorWriter)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        logger.info("Начало переноса authors");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        logger.info("Конец переноса authors");
                        return stepExecution.getExitStatus();
                    }
                })
                .listener(new ItemReadListener<>() {
                    public void onReadError(@NonNull Exception e) {
                        logger.error("Ошибка чтения sql authors", e);
                    }
                })
                .listener(new ItemWriteListener<MongoAuthor>() {
                    public void onWriteError(@NonNull Exception e, @NonNull List<MongoAuthor> list) {
                        logger.error("Ошибка записи mongo authors", e);
                    }
                })
                .listener(progressLogger)
                .build();
    }

    @Bean
    public Step migrateGenresStep(JpaCursorItemReader<SqlGenre> genreReader,
                                   MongoItemWriter<MongoGenre> genreWriter) {

        int totalGenres = ((Number) entityManager
                .createQuery("SELECT COUNT(g) FROM SqlGenre g")
                .getSingleResult()).intValue();
        ProgressLoggerChunkListener<MongoGenre> progressLogger = new ProgressLoggerChunkListener<>(totalGenres);

        return new StepBuilder("migrateGenresStep", jobRepository)
                .<SqlGenre, MongoGenre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(genreReader)
                .processor(genre -> {
                    MongoGenre mongoGenre = new MongoGenre(null, genre.getName());
                    genreCache.put(genre.getId(), mongoGenre);
                    return mongoGenre;
                })
                .writer(genreWriter)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        logger.info("Начало переноса genres");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        logger.info("Конец переноса genres");
                        return stepExecution.getExitStatus();
                    }
                })
                .listener(new ItemReadListener<>() {
                    public void onReadError(@NonNull Exception e) {
                        logger.error("Ошибка чтения sql genres", e);
                    }
                })
                .listener(new ItemWriteListener<MongoGenre>() {
                    public void onWriteError(@NonNull Exception e, @NonNull List<MongoGenre> list) {
                        logger.error("Ошибка записи mongo genres", e);
                    }
                })
                .listener(progressLogger)
                .build();
    }

    @Bean
    public Step migrateBooksStep(JpaCursorItemReader<SqlBook> bookReader,
                                 MongoItemWriter<MongoBook> bookWriter) {
        int totalBooks = ((Number) entityManager
                .createQuery("SELECT COUNT(b) FROM SqlBook b")
                .getSingleResult()).intValue();
        ProgressLoggerChunkListener<MongoBook> progressLogger = new ProgressLoggerChunkListener<>(totalBooks);

        return new StepBuilder("migrateBooksStep", jobRepository)
                .<SqlBook, MongoBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(bookReader)
                .processor(book -> {
                    MongoAuthor mongoAuthor = authorCache.get(book.getAuthorId());
                    List<MongoGenre> mongoGenres = book.getGenreIds().stream().map(genreCache::get).toList();
                    MongoBook mongoBook = new MongoBook(null, book.getTitle(), mongoAuthor, mongoGenres);
                    bookCache.put(book.getId(), mongoBook);
                    return mongoBook;
                })
                .writer(bookWriter)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        logger.info("Начало переноса books");
                    }

                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        logger.info("Конец переноса books");
                        return stepExecution.getExitStatus();
                    }
                })
                .listener(new ItemReadListener<SqlBook>() {
                    public void onReadError(@NonNull Exception e) {
                        logger.error("Ошибка чтения sql books", e);
                    }
                })
                .listener(new ItemWriteListener<MongoBook>() {
                    public void onWriteError(@NonNull Exception e, @NonNull List<MongoBook> list) {
                        logger.error("Ошибка записи mongo books", e);
                    }
                })
                .listener(progressLogger)
                .build();
    }



    @Bean
    public Job migrateLibraryJob(Step migrateAuthorsStep,
                                 Step migrateGenresStep,
                                 Step migrateBooksStep) {
        return new JobBuilder(LIBRARY_MIGRATION_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(migrateAuthorsStep)
                .next(migrateGenresStep)
                .next(migrateBooksStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

}
