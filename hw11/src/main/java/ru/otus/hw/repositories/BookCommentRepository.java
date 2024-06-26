package ru.otus.hw.repositories;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.BookComment;

public interface BookCommentRepository extends ReactiveCrudRepository<BookComment, Long> {

    Flux<BookComment> findAllByBookId(long bookId);

    @NotNull
    Mono<BookComment> findById(long id);

}
