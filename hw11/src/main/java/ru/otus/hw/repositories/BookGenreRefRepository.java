package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.BookGenreRef;

public interface BookGenreRefRepository extends ReactiveCrudRepository<BookGenreRef, Long> {

    Mono<Void> deleteByBookId(Long bookId);
}
