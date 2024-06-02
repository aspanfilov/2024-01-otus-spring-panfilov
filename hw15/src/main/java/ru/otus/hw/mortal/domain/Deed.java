package ru.otus.hw.mortal.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Deed {

    private Person person;

    private DeedType type;

    private String description;

}
