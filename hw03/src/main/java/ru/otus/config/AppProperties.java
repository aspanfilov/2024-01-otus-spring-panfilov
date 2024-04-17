package ru.otus.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "application")
@Getter
public class AppProperties implements TestFileNameProvider, TestConfig, IOConfig {

    private final String testFileName;

    private final int rightAnswersCountToPass;

    private final int maxInputAttempts;

    @ConstructorBinding
    public AppProperties(String testFileName, int rightAnswersCountToPass, int maxInputAttempts) {
        this.testFileName = testFileName;
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.maxInputAttempts = maxInputAttempts;
    }

}

