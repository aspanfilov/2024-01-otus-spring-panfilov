package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.BookCommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookCommentMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.BookCommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentRepository bookCommentRepository;

    private final BookService bookService;

    private final UserService userService;

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
        User currentUser = getCurrentUser();
        BookComment bookComment = save(0, bookId, commentText, currentUser);
        return BookCommentMapper.toBookCommentDTO(bookComment);
    }

    @Transactional
    @Override
    public BookCommentDTO update(long id, long bookId, String commentText) {
        User currentUser = getCurrentUser();
        BookComment bookComment = save(id, bookId, commentText, currentUser);
        return BookCommentMapper.toBookCommentDTO(bookComment);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookCommentRepository.deleteById(id);
    }

    private BookComment save(long id, long bookId, String commentText, User user) {
        Book book = bookService.findBookById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var bookComment = new BookComment(id, commentText, book, user);
        return bookCommentRepository.save(bookComment);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User %s not found".formatted(username)));
    }
}
