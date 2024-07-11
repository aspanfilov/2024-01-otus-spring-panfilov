package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;
import static ru.otus.hw.config.CacheConfig.ALL_BOOKS_KEY;
import static ru.otus.hw.config.CacheConfig.BOOKS_CACHE;
import static ru.otus.hw.config.CacheConfig.ENTITY_PREFIX;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Cacheable(value = BOOKS_CACHE, key = "#id")
    @Transactional(readOnly = true)
    @Override
    public Optional<BookDTO> findById(long id) {
        var book = bookRepository.findById(id);
        return book.map(BookMapper::toBookDTO);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Cacheable(value = BOOKS_CACHE, key = ENTITY_PREFIX + " + #id")
    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findBookById(long id) {
        return bookRepository.findById(id);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Cacheable(value = BOOKS_CACHE, key = ALL_BOOKS_KEY)
    @Transactional(readOnly = true)
    @Override
    public List<BookDTO> findAll() {
        List<Book> books = bookRepository.findAll();
        return BookMapper.toBookDTOList(books);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @CacheEvict(value = BOOKS_CACHE, key = ALL_BOOKS_KEY)
    @Transactional
    @Override
    public BookDTO insert(String title, long authorId, Set<Long> genresIds) {
        Book book = save(0, title, authorId, genresIds);
        return BookMapper.toBookDTO(book);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @CachePut(value = BOOKS_CACHE, key = "#id")
    @Caching(evict = {
            @CacheEvict(value = BOOKS_CACHE, key = ALL_BOOKS_KEY),
            @CacheEvict(value = BOOKS_CACHE, key = ENTITY_PREFIX + " + #id")
    })
    @Transactional
    @Override
    public BookDTO update(long id, String title, long authorId, Set<Long> genresIds) {
        Book book = save(id, title, authorId, genresIds);
        return BookMapper.toBookDTO(book);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Caching(evict = {
            @CacheEvict(value = BOOKS_CACHE, key = ALL_BOOKS_KEY),
            @CacheEvict(value = BOOKS_CACHE, key = ENTITY_PREFIX + " + #id"),
            @CacheEvict(value = BOOKS_CACHE, key = "#id")
    })
    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Transactional(readOnly = true)
    @Override
    public long getCount() {
        return bookRepository.count();
    }

    private Book save(long id, String title, long authorId, Set<Long> genresIds) {
        if (isEmpty(genresIds)) {
            throw new IllegalArgumentException("Genres ids must not be null");
        }
        var genres = genreRepository.findAllById(genresIds);
        if (isEmpty(genres) || genresIds.size() != genres.size()) {
            throw new EntityNotFoundException("One or all genres with ids %s not found".formatted(genresIds));
        }

        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));

        var book = new Book(id, title, author, genres);
        return bookRepository.save(book);
    }
}
