package ru.otus.hw.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import ru.otus.hw.config.JobConfig;

public class ProgressLoggerChunkListener<T> implements ChunkListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobConfig.LOGGER_NAME);

    private int totalItems;

    private int progressStep;

    private String entityName;

    private int lastLoggedProgressPercentage = 0;

    public ProgressLoggerChunkListener(int totalItems, int progressStep, String entityName) {
        this.totalItems = totalItems;
        this.progressStep = progressStep;
        this.entityName = entityName;
    }

    @Override
    public void afterChunk(ChunkContext context) {

        int processedItems = (int) context.getStepContext().getStepExecution().getWriteCount();

        int progressPercentage = (int) ((processedItems / (double) totalItems) * 100);

        if (progressPercentage >= lastLoggedProgressPercentage + progressStep) {
            LOGGER.info("...перенос {}: {}% ({} из {})",
                    entityName,
                    progressPercentage,
                    processedItems,
                    totalItems);
            lastLoggedProgressPercentage = progressPercentage / progressStep * progressStep;
        }

        resetProgressPercentageIfComplete();
    }

    private void resetProgressPercentageIfComplete() {
        if (lastLoggedProgressPercentage == 100) {
            lastLoggedProgressPercentage = 0;
        }
    }

}
