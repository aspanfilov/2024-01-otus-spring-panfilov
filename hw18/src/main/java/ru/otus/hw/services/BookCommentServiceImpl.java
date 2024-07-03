package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public BookCommentDTO insert(BookCommentDTO commentDTO) {
        User currentUser = getCurrentUser();
        Long bookId = commentDTO.getBook().getId();
        String commentText = commentDTO.getCommentText();

        BookComment bookComment = save(0, bookId, commentText, currentUser);
        return BookCommentMapper.toBookCommentDTO(bookComment);
    }

    @PreAuthorize("#commentDTO.user.username == authentication.name")
    @Transactional
    @Override
    public BookCommentDTO update(long id, BookCommentDTO commentDTO) {
        User currentUser = getCurrentUser();
        Long bookId = commentDTO.getBook().getId();
        String commentText = commentDTO.getCommentText();

        BookComment bookComment = save(id, bookId, commentText, currentUser);

        return BookCommentMapper.toBookCommentDTO(bookComment);
    }

    @PreAuthorize("@bookCommentServiceImpl.isCommentOwner(#id, authentication.name)")
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

    public boolean isCommentOwner(long commentId, String username) {
        return bookCommentRepository.findById(commentId)
                .map(comment -> comment.getUser().getUsername().equals(username))
                .orElse(false);
    }
}
