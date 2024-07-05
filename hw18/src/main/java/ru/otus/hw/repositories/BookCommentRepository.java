package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {

    //    @EntityGraph(attributePaths = {"book", "user"})
    @Query("SELECT bc FROM BookComment bc JOIN FETCH bc.book b JOIN FETCH b.author JOIN FETCH bc.user " +
            "WHERE bc.book.id = :bookId")
    List<BookComment> findAllByBookId(long bookId);

    //    @EntityGraph(attributePaths = {"book", "book.author", "book.genres", "user"})
    @Query("SELECT bc FROM BookComment bc JOIN FETCH bc.book b " +
            "JOIN FETCH b.author JOIN FETCH b.genres g JOIN FETCH bc.user u WHERE bc.id = :id")
    Optional<BookComment> findById(long id);

}
