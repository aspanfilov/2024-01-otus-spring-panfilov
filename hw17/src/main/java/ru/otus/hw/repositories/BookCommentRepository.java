package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {

    @EntityGraph(attributePaths = {"book", "user"})
    List<BookComment> findAllByBookId(long bookId);

    @EntityGraph(attributePaths = {"book", "book.author", "book.genres", "user"})
    Optional<BookComment> findById(long id);

}
