package ru.otus.hw.mappers;

import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.models.Author;

public class AuthorMapper {
    public static AuthorDTO toAuthorDto(Author author) {
        var authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFullName(author.getFullName());
        return authorDTO;
    }

}
