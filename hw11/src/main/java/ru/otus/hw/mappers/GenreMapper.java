package ru.otus.hw.mappers;

import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.models.Genre;

public class GenreMapper {
    public static GenreDTO toDto(Genre genre) {
        return GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public static Genre toEntity(GenreDTO genreDTO) {
        return Genre.builder()
                .id(genreDTO.getId())
                .name(genreDTO.getName())
                .build();
    }

    public static Genre toNewEntity(GenreDTO genreDTO) {
        return Genre.builder()
                .id(null)
                .name(genreDTO.getName())
                .build();
    }

}
