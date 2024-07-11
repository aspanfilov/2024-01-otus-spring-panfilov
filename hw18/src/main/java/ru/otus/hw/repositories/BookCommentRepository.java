package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository extends JpaRepository<BookComment, Long> {

    @Query("""
            SELECT bc 
            FROM BookComment bc
                 JOIN FETCH bc.user
                 JOIN FETCH bc.book b
                 JOIN FETCH b.author
            WHERE bc.book.id = :bookId
            """)
    List<BookComment> findAllByBookId(long bookId);

    @Query("""
            SELECT bc
            FROM BookComment bc
                 JOIN FETCH bc.user u
                 JOIN FETCH bc.book b
                 JOIN FETCH b.author
            WHERE bc.id = :id
            """)
    Optional<BookComment> findById(long id);

}
