package ru.otus.hw.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.lang.NonNull;

@EnableConfigurationProperties(AppProps.class)
@Configuration
public class JobConfig {

    public static final String LIBRARY_MIGRATION_JOB_NAME = "libraryMigrationJob";

    public static final String LOGGER_NAME = "Batch";

    private final Logger logger = LoggerFactory.getLogger(LOGGER_NAME);

    @Autowired
    private JobRepository jobRepository;

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
