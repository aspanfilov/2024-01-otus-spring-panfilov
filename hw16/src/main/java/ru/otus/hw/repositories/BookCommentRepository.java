package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(
        path = "book-comments",
        collectionResourceRel = "book-comments",
        itemResourceRel = "book-comment")
public interface BookCommentRepository extends JpaRepository<BookComment, Long> {

    @EntityGraph(attributePaths = {"book"})
    List<BookComment> findAllByBookId(long bookId);

    @EntityGraph(attributePaths = {"book", "book.author", "book.genres"})
    Optional<BookComment> findById(long id);

}
