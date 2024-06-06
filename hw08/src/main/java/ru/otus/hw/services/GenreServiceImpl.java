package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public List<Genre> findAllByIds(Set<String> ids) {
        return genreRepository.findAllById(ids);
    }

    @Override
    public Optional<Genre> findById(String id) {
        return genreRepository.findById(id);
    }

    @Override
    public Genre insert(String name) {
        return save(null, name);
    }

    @Override
    public Genre update(String id, String name) {
        return save(id, name);
    }

    @Override
    public void deleteById(String id) {
        genreRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        //При массовых операциях listener-ы не работают
        //      поэтому делаем проверку на существование книг жанров перед удалением жанров
        var genreIds = findAll().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        if (bookRepository.existsByGenresIdIn(genreIds)) {
            throw new IllegalStateException("Genres cannot be deleted as it is referenced by a books.");
        }
        genreRepository.deleteAll();
    }

    private Genre save(String id, String name) {
        var genre = new Genre(id, name);
        return genreRepository.save(genre);
    }
}
