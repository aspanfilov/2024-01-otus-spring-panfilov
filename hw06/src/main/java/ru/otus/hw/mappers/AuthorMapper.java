package ru.otus.hw.mappers;

import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.models.Author;

public class AuthorMapper {
    public static AuthorDTO toAuthorDto(Author author) {
        return AuthorDTO.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .build();
    }
}
