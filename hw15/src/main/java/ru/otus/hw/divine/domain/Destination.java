package ru.otus.hw.divine.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Destination {
    HEAVEN("РАЙ"),
    HELL("АД");

    private final String description;
}
