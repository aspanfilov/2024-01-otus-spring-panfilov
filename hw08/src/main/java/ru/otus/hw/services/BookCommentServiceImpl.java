package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.repositories.BookCommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentRepository bookCommentRepository;

    private final BookService bookService;

    @Override
    public List<BookComment> findAllByBookId(String bookId) {
        bookService.findById(bookId);
        return bookCommentRepository.findAllByBookId(bookId).stream()
                .toList();
    }

    @Override
    public Optional<BookComment> findById(String id) {
        return bookCommentRepository.findById(id);
    }

    @Override
    public BookComment insert(String bookId, String commentText) {
        return save(null, bookId, commentText);
    }

    @Override
    public BookComment update(String id, String bookId, String commentText) {
        return save(id, bookId, commentText);
    }

    @Override
    public void deleteById(String id) {
        bookCommentRepository.deleteById(id);
    }

    private BookComment save(String id, String bookId, String commentText) {
        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var bookComment = new BookComment(id, commentText, book);
        return bookCommentRepository.save(bookComment);
    }
}
