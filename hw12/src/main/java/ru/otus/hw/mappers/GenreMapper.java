package ru.otus.hw.mappers;

import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.models.Genre;

public class GenreMapper {
    public static GenreDTO toGenreDto(Genre genre) {
        var genreDTO = new GenreDTO();
        genreDTO.setId(genre.getId());
        genreDTO.setName(genre.getName());
        return genreDTO;
    }
}
