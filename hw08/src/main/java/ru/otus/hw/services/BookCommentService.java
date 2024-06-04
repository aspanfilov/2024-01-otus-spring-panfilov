package ru.otus.hw.services;

import ru.otus.hw.models.BookComment;

import java.util.List;
import java.util.Optional;

public interface BookCommentService {
    List<BookComment> findAllByBookId(String bookId);

    Optional<BookComment> findById(String id);

    BookComment insert(String bookId, String commentText);

    BookComment update(String id, String bookId, String commentText);

    void deleteById(String id);
}
