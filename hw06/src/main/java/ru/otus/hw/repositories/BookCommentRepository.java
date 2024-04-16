package ru.otus.hw.repositories;

import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentRepository {

    List<BookComment> findAllByBookId(long bookId);

    Optional<BookComment> findById(long id);

    BookComment save(BookComment bookComment);

    void deleteById(long id);
}
