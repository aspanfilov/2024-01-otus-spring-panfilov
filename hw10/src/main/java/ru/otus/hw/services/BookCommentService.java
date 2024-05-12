package ru.otus.hw.services;

import ru.otus.hw.dtos.BookCommentDTO;

import java.util.List;
import java.util.Optional;

public interface BookCommentService {
    List<BookCommentDTO> findAllByBookId(long bookId);

    Optional<BookCommentDTO> findById(long id);

    BookCommentDTO insert(long bookId, String commentText);

    BookCommentDTO update(long id, long bookId, String commentText);

    void deleteById(long id);
}
