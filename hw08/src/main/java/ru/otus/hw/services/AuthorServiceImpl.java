package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author insert(String fullName) {
        return save(null, fullName);
    }

    @Override
    public Author update(String id, String fullName) {
        return save(id, fullName);
    }

    private Author save(String id, String fullName) {
        var author = new Author(id, fullName);
        return authorRepository.save(author);
    }

    @Override
    public void deleteById(String id) {
        authorRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        //При массовых операциях listener-ы не работают
        //      поэтому делаем проверку на существование книг авторов перед удалением авторов
        var authorIds = findAll().stream()
                .map(Author::getId)
                .collect(Collectors.toSet());
        if (bookRepository.existsByAuthorIdIn(authorIds)) {
            throw new IllegalStateException("Authors cannot be deleted as it is referenced by a books.");
        }
        authorRepository.deleteAll();
    }

}
