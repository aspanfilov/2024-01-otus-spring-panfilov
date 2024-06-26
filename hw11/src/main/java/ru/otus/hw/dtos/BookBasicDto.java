package ru.otus.hw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookBasicDto {
    private final Long id;

    private final String title;

    private final Long authorId;
}
