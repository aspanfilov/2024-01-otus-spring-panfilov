package ru.otus.hw.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "app")
@Data
public class AppProps {

    private final int chunkSize;

    private final int progressLogStepPercentage;

    @ConstructorBinding
    public AppProps(int chunkSize, int progressLogStepPercentage) {
        this.chunkSize = chunkSize;
        this.progressLogStepPercentage = progressLogStepPercentage;
    }
}
