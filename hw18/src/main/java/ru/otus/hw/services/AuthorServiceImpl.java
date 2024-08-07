package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

import static ru.otus.hw.config.CacheConfig.ALL_AUTHORS_KEY;
import static ru.otus.hw.config.CacheConfig.AUTHORS_CACHE;
import static ru.otus.hw.config.CacheConfig.BOOKS_CACHE;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Cacheable(value = AUTHORS_CACHE, key = ALL_AUTHORS_KEY)
    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Cacheable(value = AUTHORS_CACHE, key = "#id")
    @Transactional(readOnly = true)
    @Override
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @CacheEvict(value = AUTHORS_CACHE, key = ALL_AUTHORS_KEY)
    @Transactional
    @Override
    public Author insert(String fullName) {
        return save(0, fullName);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @CachePut(value = AUTHORS_CACHE, key = "#id")
    @Caching(evict = {
            @CacheEvict(value = AUTHORS_CACHE, key = ALL_AUTHORS_KEY),
            @CacheEvict(value = BOOKS_CACHE, allEntries = true)
    })
    @Transactional
    @Override
    public Author update(long id, String fullName) {
        return save(id, fullName);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Caching(evict = {
            @CacheEvict(value = AUTHORS_CACHE, key = "#id"),
            @CacheEvict(value = AUTHORS_CACHE, key = ALL_AUTHORS_KEY),
            @CacheEvict(value = BOOKS_CACHE, allEntries = true)
    })
    @Transactional
    @Override
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Transactional(readOnly = true)
    @Override
    public long getCount() {
        return authorRepository.count();
    }

    private Author save(long id, String fullName) {
        var author = new Author(id, fullName);
        return authorRepository.save(author);
    }

}
