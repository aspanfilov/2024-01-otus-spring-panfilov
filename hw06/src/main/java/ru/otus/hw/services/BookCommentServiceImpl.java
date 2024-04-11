package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.BookCommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookCommentMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.repositories.BookCommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentRepository bookCommentRepository;

    private final BookService bookService;

    @Transactional(readOnly = true)
    @Override
    public List<BookCommentDTO> findAllByBookId(long bookId) {
        bookService.findById(bookId);
        return bookCommentRepository.findAllByBookId(bookId).stream().map(BookCommentMapper::toBookCommentDTO).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookCommentDTO> findById(long id) {
        return bookCommentRepository.findById(id).map(BookCommentMapper::toBookCommentDTO);
    }

    @Transactional
    @Override
    public BookCommentDTO insert(long bookId, String commentText) {
        BookComment bookComment = save(0, bookId, commentText);
        return BookCommentMapper.toBookCommentDTO(bookComment);
    }

    @Transactional
    @Override
    public BookCommentDTO update(long id, long bookId, String commentText) {
        BookComment bookComment = save(id, bookId, commentText);
        return BookCommentMapper.toBookCommentDTO(bookComment);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookCommentRepository.deleteById(id);
    }

    private BookComment save(long id, long bookId, String commentText) {
        Book book = bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var bookComment = new BookComment(id, commentText, book);
        return bookCommentRepository.save(bookComment);
    }
}
