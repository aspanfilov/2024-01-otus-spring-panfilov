package ru.otus.hw.mappers;

import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.BookRequestDTO;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.Collectors;

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

    public static BookRequestDTO toBookRequestDTO(Book book) {
        var bookRequest = new BookRequestDTO();
        bookRequest.setId(book.getId());
        bookRequest.setTitle(book.getTitle());
        bookRequest.setAuthorId(book.getAuthor().getId());
        bookRequest.setGenreIds(
                book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
        );
        return bookRequest;
    }
}
