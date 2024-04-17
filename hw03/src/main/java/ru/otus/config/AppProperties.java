package ru.otus.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Locale;
import java.util.Map;

@ConfigurationProperties(prefix = "test")
@Getter()
public class AppProperties implements TestFileNameProvider, TestConfig, IOConfig, LocaleConfig {

    private final String testFileName;

    private final int rightAnswersCountToPass;

    private final int maxInputAttempts;

    private final Map<String, String> fileNameByLocaleTag;

    private final Locale locale;

    @ConstructorBinding
    public AppProperties(String testFileName,
                         int rightAnswersCountToPass,
                         int maxInputAttempts,
                         Map<String, String> fileNameByLocaleTag,
                         String locale) {
        this.testFileName = testFileName;
        this.rightAnswersCountToPass = rightAnswersCountToPass;
        this.maxInputAttempts = maxInputAttempts;
        this.fileNameByLocaleTag = fileNameByLocaleTag;
        this.locale = Locale.forLanguageTag(locale);
    }

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }

}

