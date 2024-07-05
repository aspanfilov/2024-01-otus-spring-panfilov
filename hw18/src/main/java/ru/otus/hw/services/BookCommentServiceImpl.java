package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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

import static ru.otus.hw.config.CacheConfig.BOOK_COMMENTS_CACHE;
import static ru.otus.hw.config.CacheConfig.BOOK_ID_PREFIX;

@Service
@CircuitBreaker(name = "dbCircuitBreaker")
@RequiredArgsConstructor
public class BookCommentServiceImpl implements BookCommentService {
    private final BookCommentRepository bookCommentRepository;

    private final BookService bookService;

    private final UserService userService;

    @Cacheable(value = BOOK_COMMENTS_CACHE, key = BOOK_ID_PREFIX + " + #bookId")
    @Transactional(readOnly = true)
    @Override
    public List<BookCommentDTO> findAllByBookId(long bookId) {
        bookService.findById(bookId);
        return bookCommentRepository.findAllByBookId(bookId).stream().map(BookCommentMapper::toBookCommentDTO).toList();
    }

    @Cacheable(value = BOOK_COMMENTS_CACHE, key = "#id")
    @Transactional(readOnly = true)
    @Override
    public Optional<BookCommentDTO> findById(long id) {
        return bookCommentRepository.findById(id).map(BookCommentMapper::toBookCommentDTO);
    }

    @CacheEvict(value = BOOK_COMMENTS_CACHE, key = BOOK_ID_PREFIX + " + #commentDTO.book.id")
    @Transactional
    @Override
    public BookCommentDTO insert(BookCommentDTO commentDTO) {

        User currentUser = getCurrentUser();
        Long bookId = commentDTO.getBook().getId();
        String commentText = commentDTO.getCommentText();

        BookComment bookComment = save(0, bookId, commentText, currentUser);
        return BookCommentMapper.toBookCommentDTO(bookComment);
    }

    @CachePut(value = BOOK_COMMENTS_CACHE, key = "#id")
    @CacheEvict(value = BOOK_COMMENTS_CACHE, key = BOOK_ID_PREFIX + " + #commentDTO.book.id")
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

    @Caching(evict = {
            @CacheEvict(value = BOOK_COMMENTS_CACHE, key = "#id"),
            @CacheEvict(value = BOOK_COMMENTS_CACHE, key = BOOK_ID_PREFIX + " + #bookId")
    })
    @PreAuthorize("@bookCommentServiceImpl.isCommentOwner(#id, authentication.name)")
    @Transactional
    @Override
    public void deleteById(long id, long bookId) {
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

