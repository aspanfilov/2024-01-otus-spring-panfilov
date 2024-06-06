package ru.otus.hw.mappers;

import ru.otus.hw.mortal.domain.Deed;
import ru.otus.hw.divine.domain.KarmaPoint;

public class KarmaConverter {

    public static KarmaPoint deedToKarmaPoint(Deed deed) {
        int value = switch (deed.getType()) {
            case GOOD -> 1;
            case BAD -> -1;
            default -> 0;
        };

        return new KarmaPoint(deed.getPerson(), value);
    }

}
