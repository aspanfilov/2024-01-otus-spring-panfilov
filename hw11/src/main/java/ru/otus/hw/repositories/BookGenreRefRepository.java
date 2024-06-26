package ru.otus.hw.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.BookGenreRef;

public interface BookGenreRefRepository extends ReactiveCrudRepository<BookGenreRef, Long> {

    Flux<Void> deleteByBookId(Long bookId);
}
