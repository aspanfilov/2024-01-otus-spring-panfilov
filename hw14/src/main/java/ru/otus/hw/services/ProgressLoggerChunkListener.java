package ru.otus.hw.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class ProgressLoggerChunkListener<T> implements ChunkListener {

    //CHUNK и STEP передавать из пропертей

    private static final Logger logger = LoggerFactory.getLogger("Batch");

    private int totalItems;

    private long processedItems = 0;

    public ProgressLoggerChunkListener(int totalItems) {
        this.totalItems = totalItems;
    }

    @Override
    public void afterChunk(ChunkContext context) {

        long chunkSize = 2;

        processedItems += chunkSize;

        int progressPercentage = (int) ((processedItems / (double) totalItems) * 100);

        if (progressPercentage / 10 > (processedItems - chunkSize) / (double) totalItems * 10) {
            //возможно в многопоточке передавать все снаружи в том числе и именование энтити для которого выводится прогресс
            logger.info("Прогресс: {}% ({} из {})", progressPercentage, processedItems, totalItems);
        }
    }

}
