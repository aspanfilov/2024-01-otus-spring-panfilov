package ru.otus.hw.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "app")
@Getter
public class AppProps {
    private final boolean parallelProcessing;

    @ConstructorBinding
    public AppProps(boolean parallelProsessing) {
        this.parallelProcessing = parallelProsessing;
    }

}
