package ru.otus.hw.mappers;

import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.models.Author;

public class AuthorMapper {
    public static AuthorDTO toDto(Author author) {
        return AuthorDTO.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .build();
    }

    public static Author toEntity(AuthorDTO authorDTO) {
        return Author.builder()
                .id(authorDTO.getId())
                .fullName(authorDTO.getFullName())
                .build();
    }

    public static Author toNewEntity(AuthorDTO authorDTO) {
        return Author.builder()
                .id(null)
                .fullName(authorDTO.getFullName())
                .build();
    }

}
