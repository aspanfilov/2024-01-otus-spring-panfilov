package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.BookDetailDTO;
import ru.otus.hw.dtos.BookReferenceDTO;
import ru.otus.hw.models.Book;

public interface BookHandler {

    Flux<BookDetailDTO> findAll();

    Mono<Book> saveBook(Mono<BookReferenceDTO> bookRequestMono);

}
