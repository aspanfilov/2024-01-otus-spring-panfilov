package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.divine.domain.Soul;
import ru.otus.hw.domain.Intention;

import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
@Slf4j
public class LifeCyclesServiceImpl implements LifeCyclesService {

    private static final int LIFE_LIMIT = 5;

    private final LifeGateway lifeGateway;

    @Override
    public void startLifeCycleGenerator() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < LIFE_LIMIT; i++) {
            pool.execute(() -> {
                Soul soul = lifeGateway.execute(new Intention());
                log.info("!!! {} {}: ДУША ЧЕЛОВЕКА ОТПРАВЛЕНА В {}:",
                        soul.getUsedPerson().getId(),
                        soul.getUsedPerson().getName(),
                        soul.getDestination().getDescription());

            });
        }
    }

}
