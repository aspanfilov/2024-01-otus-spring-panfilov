package ru.otus.hw.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.BookDetailDTO;
import ru.otus.hw.dtos.BookReferenceDTO;

public interface BookRepositoryCustom {

    Flux<BookDetailDTO> findAll();

    Mono<BookReferenceDTO> findById(Long id);

}
