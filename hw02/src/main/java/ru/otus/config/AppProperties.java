package ru.otus.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
@Getter
public class AppProperties implements TestFileNameProvider {

    private final String testFileName;

    public AppProperties(@Value("${application.test-file-name}") String testFileName) {
        this.testFileName = testFileName;
    }
}
