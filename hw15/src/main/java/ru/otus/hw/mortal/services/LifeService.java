package ru.otus.hw.mortal.services;

import ru.otus.hw.mortal.domain.Person;
import ru.otus.hw.mortal.domain.PersonLife;

public interface LifeService {
    PersonLife liveLife(Person person);
}
