package ru.otus.hw.mappers;

import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.models.Book;

import java.util.List;

public class BookMapper {
    public static BookDTO toBookDTO(Book book) {

        List<GenreDTO> genreDTOS = book.getGenres().stream().map(GenreMapper::toGenreDto).toList();

        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(AuthorMapper.toAuthorDto(book.getAuthor()))
                .genres(genreDTOS)
                .build();
    }

    public static List<BookDTO> toBookDTOList(List<Book> books) {
        return books.stream().map(BookMapper::toBookDTO).toList();
    }
}
