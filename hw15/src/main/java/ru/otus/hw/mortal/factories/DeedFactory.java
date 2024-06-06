package ru.otus.hw.mortal.factories;

import ru.otus.hw.mortal.domain.Deed;
import ru.otus.hw.mortal.domain.Person;

public interface DeedFactory {
    Deed doDeed(Person person);

    Deed doOriginalSin(Person person);
}
