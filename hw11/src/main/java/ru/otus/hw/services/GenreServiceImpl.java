package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
//    private final GenreRepository genreRepository;
//
//    @Transactional(readOnly = true)
//    @Override
//    public List<Genre> findAll() {
//        return genreRepository.findAll();
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public List<Genre> findAllByIds(Set<Long> ids) {
//        return genreRepository.findAllById(ids);
//    }
//
//    @Transactional(readOnly = true)
//    @Override
//    public Optional<Genre> findById(long id) {
//        return genreRepository.findById(id);
//    }
//
//    @Transactional
//    @Override
//    public Genre insert(String name) {
//        return save(0, name);
//    }
//
//    @Transactional
//    @Override
//    public Genre update(long id, String name) {
//        return save(id, name);
//    }
//
//    @Transactional
//    @Override
//    public void deleteById(long id) {
//        genreRepository.deleteById(id);
//    }
//
//    private Genre save(long id, String name) {
//        var genre = new Genre(id, name);
//        return genreRepository.save(genre);
//    }
}
