package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    @Override
    public Optional<Author> findById(long id) {
        return authorRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Transactional
    @Override
    public Author insert(String fullName) {
        return save(0, fullName);
    }

    @Transactional
    @Override
    public Author update(long id, String fullName) {
        return save(id, fullName);
    }

    private Author save(long id, String fullName) {
        var author = new Author(id, fullName);
        return authorRepository.save(author);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        authorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public long getCount() {
        return authorRepository.count();
    }
}
