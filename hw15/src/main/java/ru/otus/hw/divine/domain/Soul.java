package ru.otus.hw.divine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.mortal.domain.Person;

@Data
@AllArgsConstructor
public class Soul {

    private Person usedPerson;

    private int karma;

    private Destination destination;
}
