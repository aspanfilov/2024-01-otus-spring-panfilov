package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.otus.hw.config.CacheConfig.ALL_GENRES_KEY;
import static ru.otus.hw.config.CacheConfig.BOOKS_CACHE;
import static ru.otus.hw.config.CacheConfig.GENRES_CACHE;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Cacheable(value = GENRES_CACHE, key = ALL_GENRES_KEY)
    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Cacheable(value = GENRES_CACHE, key = "#ids")
    @Transactional(readOnly = true)
    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return genreRepository.findAllById(ids);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Cacheable(value = GENRES_CACHE, key = "#id")
    @Transactional(readOnly = true)
    @Override
    public Optional<Genre> findById(long id) {
        return genreRepository.findById(id);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @CacheEvict(value = GENRES_CACHE, key = ALL_GENRES_KEY)
    @Transactional
    @Override
    public Genre insert(String name) {
        return save(0, name);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Caching(evict = {
            @CacheEvict(value = GENRES_CACHE, allEntries = true),
            @CacheEvict(value = BOOKS_CACHE, allEntries = true)
    })
    @Transactional
    @Override
    public Genre update(long id, String name) {
        return save(id, name);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Caching(evict = {
            @CacheEvict(value = GENRES_CACHE, allEntries = true),
            @CacheEvict(value = BOOKS_CACHE, allEntries = true)
    })
    @Transactional
    @Override
    public void deleteById(long id) {
        genreRepository.deleteById(id);
    }

    @CircuitBreaker(name = "dbCircuitBreaker")
    @Transactional(readOnly = true)
    @Override
    public long getCount() {
        return genreRepository.count();
    }

    private Genre save(long id, String name) {
        var genre = new Genre(id, name);
        return genreRepository.save(genre);
    }
}
