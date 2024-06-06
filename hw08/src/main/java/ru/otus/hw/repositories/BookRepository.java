package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Book;

import java.util.Set;

public interface BookRepository extends MongoRepository<Book, String> {
    boolean existsByAuthorId(String authorId);

    boolean existsByGenresId(String genreId);

    boolean existsByAuthorIdIn(Set<String> authorIds);

    boolean existsByGenresIdIn(Set<String> genreIds);
}
