package ru.otus.hw.mappers;

import ru.otus.hw.dtos.BookReferenceDTO;
import ru.otus.hw.models.Book;

public class BookMapper {
    public static Book toEntity(BookReferenceDTO bookRequest) {

        return new Book(
                bookRequest.getId(),
                bookRequest.getTitle(),
                bookRequest.getAuthorId());

//        return Book.builder()
//                .id(bookRequest.getId())
//                .title(bookRequest.getTitle())
//                .authorId(bookRequest.getAuthorId())
//                .build();
    }


//    public static BookDTO toDto(Book book) {
//
//        List<GenreDTO> genreDTOs = book.getGenres().stream()
//                .map(GenreMapper::toDto).toList();
//
//        return BookDTO.builder()
//                .id(book.getId())
//                .title(book.getTitle())
//                .author(AuthorMapper.toDto(book.getAuthor()))
//                .genres(genreDTOs)
//                .build();
//    }
//
//    public static List<BookDTO> toBookDTOList(List<Book> books) {
//        return books.stream().map(BookMapper::toDto).toList();
//    }
//
//    public static BookRequestDTO toBookRequestDTO(Book book) {
//        var bookRequest = new BookRequestDTO();
//        bookRequest.setId(book.getId());
//        bookRequest.setTitle(book.getTitle());
//        bookRequest.setAuthorId(book.getAuthor().getId());
//        bookRequest.setGenreIds(
//                book.getGenres().stream().map(Genre::getId).collect(Collectors.toSet())
//        );
//        return bookRequest;
//    }
}
