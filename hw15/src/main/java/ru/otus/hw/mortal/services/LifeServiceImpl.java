package ru.otus.hw.mortal.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.mortal.domain.Deed;
import ru.otus.hw.mortal.domain.DeedType;
import ru.otus.hw.mortal.domain.Person;
import ru.otus.hw.mortal.domain.PersonLife;
import ru.otus.hw.mortal.factories.DeedFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service(value = "lifeService")
@AllArgsConstructor
@Slf4j
public class LifeServiceImpl implements LifeService {

    private static final int LIFE_LIMIT = 10;

    private static final int MAX_TIME_PERIOD = 4000;

    private static final Random RANDOM = new Random();

    private final DeedFactory deedFactory;

    @Override
    public PersonLife liveLife(Person person) {

        log.info(">>> {} {}: НОВЫЙ ЧЕЛОВЕК РОДИЛСЯ (-)",
                person.getId(), person.getName());

        List<Deed> deeds = new ArrayList<>();
        deeds.add(deedFactory.doOriginalSin(person));

        int deedCount = RANDOM.nextInt(LIFE_LIMIT) + 1;
        for (int i = 0; i < deedCount; i++) {
            Deed deed = deedFactory.doDeed(person);
            deeds.add(deed);
            log.info("{} {}: человек совершил поступок - {}{}",
                    person.getId(), person.getName(),
                    deed.getDescription(), getDeedMark(deed));
            delay();
        }

        log.info("<<< {} {}: ЧЕЛОВЕК СКОНЧАЛСЯ. благодеяний {}, грешков {}",
                person.getId(), person.getName(),
                countDeeds(deeds, DeedType.GOOD),
                countDeeds(deeds, DeedType.BAD));

        return new PersonLife(person, deeds);
    }

    private static void delay() {
        try {
            Thread.sleep(RANDOM.nextInt(MAX_TIME_PERIOD));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getDeedMark(Deed deed) {
        return switch (deed.getType()) {
            case GOOD -> "(+)";
            case BAD -> "(-)";
            case NEUTRAL -> "( )";
        };
    }

    private static long countDeeds(List<Deed> deeds, DeedType deedType) {
        return deeds.stream()
                .filter(deed -> deed.getType().equals(deedType))
                .count();
    }

}
