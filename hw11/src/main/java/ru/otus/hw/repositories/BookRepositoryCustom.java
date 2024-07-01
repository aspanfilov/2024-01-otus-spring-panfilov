package ru.otus.hw.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.book.BookDetailDTO;
import ru.otus.hw.models.Book;

public interface BookRepositoryCustom {

    Flux<BookDetailDTO> findAll();

    Mono<Book> findById(Long id);

}
