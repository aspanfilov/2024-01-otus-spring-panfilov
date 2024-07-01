package ru.otus.hw.handlers;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.book.BookBasicDto;
import ru.otus.hw.dtos.book.BookDetailDTO;
import ru.otus.hw.dtos.book.BookReferenceDTO;
import ru.otus.hw.models.Book;

public interface BookHandler {

    Flux<BookDetailDTO> getBooks();

    Mono<ResponseEntity<Book>> getBook(Long id);

    Mono<ResponseEntity<BookBasicDto>> createBook(BookReferenceDTO bookReferenceDTO);

    Mono<ResponseEntity<BookBasicDto>> updateBook(Long id, BookReferenceDTO bookReferenceDTO);

    Mono<ResponseEntity<Void>> deleteBook(Long id);
}
