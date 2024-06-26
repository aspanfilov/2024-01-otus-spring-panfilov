package ru.otus.hw.mappers;

import ru.otus.hw.dtos.book.BookBasicDto;
import ru.otus.hw.dtos.book.BookReferenceDTO;
import ru.otus.hw.models.Book;

public class BookMapper {
    public static Book toEntity(BookReferenceDTO bookRequest) {

        return new Book(
                bookRequest.getId(),
                bookRequest.getTitle(),
                bookRequest.getAuthorId());
    }

    public static BookBasicDto toBookBasicDto(Book book) {

        return new BookBasicDto(
                book.getId(),
                book.getTitle(),
                book.getAuthorId());
    }

}
