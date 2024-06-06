package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LifeCycleGeneratorRunner implements CommandLineRunner {

    private final LifeCyclesService lifeCyclesService;

    @Override
    public void run(String... args) {
        lifeCyclesService.startLifeCycleGenerator();
    }
}
