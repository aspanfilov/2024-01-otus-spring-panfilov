package ru.otus.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppProperties implements TestFileNameProvider, TestConfig, IOConfig {

    private final String testFileName;

    private final int rightAnswersCountToPass;

    private final int maxInputAttempts;

    public AppProperties(@Value("${application.test-file-name}") String testFileName,
                         @Value("${application.right-answers-count-to-pass}") int rightAnswersCountToPass,
                         @Value("${application.max-input-attempts}") int maxInputAttempts) {
        this.testFileName = testFileName;
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.maxInputAttempts = maxInputAttempts;
    }
}
