package ru.otus.hw.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoBookComment;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.models.sql.SqlAuthor;
import ru.otus.hw.models.sql.SqlBook;
import ru.otus.hw.models.sql.SqlBookComment;
import ru.otus.hw.models.sql.SqlGenre;
import ru.otus.hw.services.BooksGenresCacheLoaderService;
import ru.otus.hw.services.MongoDBCleanerService;
import ru.otus.hw.services.ProgressLoggerChunkListener;

import java.util.List;

@EnableConfigurationProperties(AppProps.class)
@Configuration
public class JobConfig {

    public static final String LIBRARY_MIGRATION_JOB_NAME = "libraryMigrationJob";

    public static final String LOGGER_NAME = "Batch";

    public static final String AUTHORS = "authors";

    public static final String GENRES = "genres";

    public static final String BOOKS = "books";

    public static final String COMMENTS = "comments";

    private final Logger logger = LoggerFactory.getLogger(LOGGER_NAME);

    @Autowired
    private AppProps appProps;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BooksGenresCacheLoaderService booksGenresCacheLoaderService;

    @Autowired
    private MongoDBCleanerService mongoDBCleanerService;

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
    public MongoItemWriter<MongoAuthor> authorWriter(MongoTemplate mongoTemplate) {

        return new MongoItemWriterBuilder<MongoAuthor>()
                .template(mongoTemplate)
                .collection("authors")
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
    public MongoItemWriter<MongoGenre> genreWriter(MongoTemplate mongoTemplate) {

        return new MongoItemWriterBuilder<MongoGenre>()
                .template(mongoTemplate)
                .collection("genres")
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
    public MongoItemWriter<MongoBook> bookWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<MongoBook>()
                .template(mongoTemplate)
                .collection("books")
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

    @Bean
    public MongoItemWriter<MongoBookComment> bookCommentWriter(MongoTemplate mongoTemplate) {
        return new MongoItemWriterBuilder<MongoBookComment>()
                .template(mongoTemplate)
                .collection("book_comments")
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter mongoDBCleanerTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(mongoDBCleanerService);
        adapter.setTargetMethod("cleanMongoDB");

        return adapter;
    }

    @Bean
    public MethodInvokingTaskletAdapter booksGenresCacheLoaderTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(booksGenresCacheLoaderService);
        adapter.setTargetMethod("loadBooksGenresCache");

        return adapter;
    }

    private StepExecutionListener createStepExecutionListener(String entityName) {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                logger.info("Начало переноса {}", entityName);
            }
        };
    }

    private ItemReadListener<SqlAuthor> createItemReadListener(String entityName) {
        return new ItemReadListener<>() {
            public void onReadError(@NonNull Exception e) {
                logger.error("Ошибка чтения sql {}", entityName, e);
            }
        };
    }

    private <T> ItemWriteListener<T> createItemWriteListener(String entityName) {
        return new ItemWriteListener<T>() {
            public void onWriteError(@NonNull Exception e, @NonNull List<T> list) {
                logger.error("Ошибка записи mongo {}", entityName, e);
            }
        };
    }

    @Bean
    public Step migrateAuthorsStep(JpaPagingItemReader<SqlAuthor> authorReader,
                                   MongoItemWriter<MongoAuthor> authorWriter,
                                   @Autowired
                                   ItemProcessor<SqlAuthor, MongoAuthor> authorProcessor) {

        int totalAuthors = ((Number) entityManager
                .createQuery("SELECT COUNT(a) FROM SqlAuthor a")
                .getSingleResult()).intValue();

        ProgressLoggerChunkListener<MongoAuthor> progressLogger = new ProgressLoggerChunkListener<>(
                totalAuthors,
                appProps.getProgressLogStepPercentage(),
                AUTHORS);

        return new StepBuilder("migrateAuthorsStep", jobRepository)
                .<SqlAuthor, MongoAuthor>chunk(appProps.getChunkSize(), platformTransactionManager)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .listener(createStepExecutionListener(AUTHORS))
                .listener(createItemReadListener(AUTHORS))
                .listener(createItemWriteListener(AUTHORS))
                .listener(progressLogger)
                .build();
    }

    @Bean
    public Step migrateGenresStep(JpaPagingItemReader<SqlGenre> genreReader,
                                  MongoItemWriter<MongoGenre> genreWriter,
                                  @Autowired
                                  ItemProcessor<SqlGenre, MongoGenre> genreProcessor) {

        int totalGenres = ((Number) entityManager
                .createQuery("SELECT COUNT(g) FROM SqlGenre g")
                .getSingleResult()).intValue();
        ProgressLoggerChunkListener<MongoGenre> progressLogger = new ProgressLoggerChunkListener<>(
                totalGenres,
                appProps.getProgressLogStepPercentage(),
                GENRES);

        return new StepBuilder("migrateGenresStep", jobRepository)
                .<SqlGenre, MongoGenre>chunk(appProps.getChunkSize(), platformTransactionManager)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
                .listener(createStepExecutionListener(GENRES))
                .listener(createItemReadListener(GENRES))
                .listener(createItemWriteListener(GENRES))
                .listener(progressLogger)
                .build();
    }

    @Bean
    public Step cleanMongoDBStep() {
        return new StepBuilder("cleanMongoDBStep", jobRepository)
                .tasklet(mongoDBCleanerTasklet(), platformTransactionManager)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        logger.info("Начало очистки таблиц mongoDB");
                    }
                })
                .build();
    }

    @Bean
    public Step booksGenresCacheLoadStep() {
        return new StepBuilder("booksGenresCacheLoadStep", jobRepository)
                .tasklet(booksGenresCacheLoaderTasklet(), platformTransactionManager)
                .listener(new StepExecutionListener() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        logger.info("Начало загрузки booksGenresCache");
                    }
                })
                .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    @Bean
    public Step migrateBooksStep(JpaPagingItemReader<SqlBook> bookReader,
                                 MongoItemWriter<MongoBook> bookWriter,
                                 @Autowired
                                 ItemProcessor<SqlBook, MongoBook> bookProcessor) {
        int totalBooks = ((Number) entityManager
                .createQuery("SELECT COUNT(b) FROM SqlBook b")
                .getSingleResult()).intValue();
        ProgressLoggerChunkListener<MongoBook> progressLogger = new ProgressLoggerChunkListener<>(
                totalBooks,
                appProps.getProgressLogStepPercentage(),
                BOOKS);

        return new StepBuilder("migrateBooksStep", jobRepository)
                .<SqlBook, MongoBook>chunk(appProps.getChunkSize(), platformTransactionManager)
                .reader(bookReader)
                .processor(bookProcessor)
                .writer(bookWriter)
                .listener(createStepExecutionListener(BOOKS))
                .listener(createItemReadListener(BOOKS))
                .listener(createItemWriteListener(BOOKS))
                .listener(progressLogger)
                .build();
    }

    @SuppressWarnings("checkstyle:MethodLength")
    @Bean
    public Step migrateBookCommentsStep(JpaPagingItemReader<SqlBookComment> bookCommentReader,
                                        MongoItemWriter<MongoBookComment> bookCommentWriter,
                                        @Autowired
                                        ItemProcessor<SqlBookComment, MongoBookComment> bookCommentProcessor) {
        int totalBookComments = ((Number) entityManager
                .createQuery("SELECT COUNT(bc) FROM SqlBookComment bc")
                .getSingleResult()).intValue();
        ProgressLoggerChunkListener<MongoBookComment> progressLogger = new ProgressLoggerChunkListener<>(
                totalBookComments,
                appProps.getProgressLogStepPercentage(),
                COMMENTS);

        return new StepBuilder("migrateBookCommentsStep", jobRepository)
                .<SqlBookComment, MongoBookComment>chunk(appProps.getChunkSize(), platformTransactionManager)
                .reader(bookCommentReader)
                .processor(bookCommentProcessor)
                .writer(bookCommentWriter)
                .listener(createStepExecutionListener(COMMENTS))
                .listener(createItemReadListener(COMMENTS))
                .listener(createItemWriteListener(COMMENTS))
                .listener(progressLogger)
                .build();
    }

    @Bean
    public Flow cleanMongoDBFlow(Step cleanMongoDBStep) {
        return new FlowBuilder<SimpleFlow>("cleanMongoDBFlow")
                .start(cleanMongoDBStep)
                .build();
    }

    @Bean
    public Flow migrateAuthorsFlow(Step migrateAuthorsStep) {
        return new FlowBuilder<SimpleFlow>("migrateAuthorsFlow")
                .start(migrateAuthorsStep)
                .build();
    }

    @Bean
    public Flow migrateGenresFlow(Step migrateGenresStep) {
        return new FlowBuilder<SimpleFlow>("migrateGenresFlow")
                .start(migrateGenresStep)
                .build();
    }

    @Bean
    public Flow migrateAuthorsGenresSplitFlow(Flow migrateAuthorsFlow, Flow migrateGenresFlow) {
        return new FlowBuilder<SimpleFlow>("migrateAuthorsGenresSplitFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(migrateAuthorsFlow, migrateGenresFlow)
                .build();
    }

    @Bean
    public Job migrateLibraryJob(Flow cleanMongoDBFlow,
                                 Flow migrateAuthorsGenresSplitFlow,
                                 Step booksGenresCacheLoadStep,
                                 Step migrateBooksStep,
                                 Step migrateBookCommentsStep) {
        return new JobBuilder(LIBRARY_MIGRATION_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(cleanMongoDBFlow)
                .next(migrateAuthorsGenresSplitFlow)
                .next(booksGenresCacheLoadStep)
                .next(migrateBooksStep)
                .next(migrateBookCommentsStep)
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
