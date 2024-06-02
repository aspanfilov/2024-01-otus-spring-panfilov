package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.mortal.domain.Person;
import ru.otus.hw.divine.domain.Soul;
import ru.otus.hw.mortal.factories.PersonFactory;

import java.util.concurrent.ForkJoinPool;

@Service
@RequiredArgsConstructor
@Slf4j
public class LifeCyclesServiceImpl implements LifeCyclesService {

    private static final int PERSONS_LIMIT = 5;

    private final LifeGateway lifeGateway;

    private final PersonFactory personFactory;

    @Override
    public void startLifeCycleGenerator() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < PERSONS_LIMIT; i++) {
            pool.execute(() -> {
                Person person = personFactory.createPerson();
                Soul soul = lifeGateway.process(person);
                log.info("!!! {} {}: ДУША ЧЕЛОВЕКА ОТПРАВЛЕНА В {}:",
                        soul.getUsedPerson().getId(),
                        soul.getUsedPerson().getName(),
                        soul.getDestination().getDescription());

            });
        }
    }

}
