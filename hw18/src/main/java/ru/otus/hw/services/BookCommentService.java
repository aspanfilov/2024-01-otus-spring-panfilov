package ru.otus.hw.services;

import ru.otus.hw.dtos.BookCommentDTO;

import java.util.List;
import java.util.Optional;

public interface BookCommentService {
    List<BookCommentDTO> findAllByBookId(long bookId);

    Optional<BookCommentDTO> findById(long id);

    BookCommentDTO insert(BookCommentDTO bookCommentDTO);

    BookCommentDTO update(long id, BookCommentDTO bookCommentDTO);

    void deleteById(long id, long bookId);
}
