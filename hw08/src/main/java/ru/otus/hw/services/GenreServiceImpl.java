package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

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
        //todo повесить слушателей которые будут проверять есть ли у этого жанра книги
        genreRepository.deleteById(id);
    }

    private Genre save(String id, String name) {
        var genre = new Genre(id, name);
        return genreRepository.save(genre);
    }
}
