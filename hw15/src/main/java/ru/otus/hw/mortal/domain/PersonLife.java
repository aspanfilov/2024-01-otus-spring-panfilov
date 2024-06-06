package ru.otus.hw.mortal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PersonLife {

    private final Person person;

    private final List<Deed> deeds;

}
