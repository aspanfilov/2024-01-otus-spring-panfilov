package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Book;

public interface BookRepository extends MongoRepository<Book, String> {
    boolean existsByAuthorId(String authorId);

    boolean existsByGenresId(String genreId);
}
