package ru.otus.hw.controllers.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.book.BookBasicDto;
import ru.otus.hw.dtos.book.BookDetailDTO;
import ru.otus.hw.dtos.book.BookReferenceDTO;
import ru.otus.hw.handlers.BookHandler;
import ru.otus.hw.models.Book;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookHandler bookHandler;

    @GetMapping("/api/v1/books")
    public Flux<BookDetailDTO> getBooks() {
        return bookHandler.getBooks();
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<ResponseEntity<Book>> getBook(@PathVariable("id") Long id) {
        return bookHandler.getBook(id);
    }

    @PostMapping("/api/v1/books")
    public Mono<ResponseEntity<BookBasicDto>> createBook(@RequestBody @Valid BookReferenceDTO bookReferenceDTO) {
        return bookHandler.createBook(bookReferenceDTO);
    }

    @PutMapping("/api/v1/books/{id}")
    public Mono<ResponseEntity<BookBasicDto>> updateBook(@PathVariable Long id,
                                                         @RequestBody @Valid BookReferenceDTO bookReferenceDTO) {
        return bookHandler.updateBook(id, bookReferenceDTO);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") Long id) {
        return bookHandler.deleteBook(id);
    }

}
