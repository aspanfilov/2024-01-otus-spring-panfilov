package ru.otus.hw.mappers;

import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.models.Genre;

public class GenreMapper {
    public static GenreDTO toGenreDto(Genre genre) {
        return GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
