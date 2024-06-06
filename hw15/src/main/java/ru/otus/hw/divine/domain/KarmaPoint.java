package ru.otus.hw.divine.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.mortal.domain.Person;

@Data
@AllArgsConstructor
public class KarmaPoint {

    private Person person;

    private final int value;

}
