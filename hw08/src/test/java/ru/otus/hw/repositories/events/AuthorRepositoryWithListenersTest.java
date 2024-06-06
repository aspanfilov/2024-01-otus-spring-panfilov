package ru.otus.hw.repositories.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoOperations;
import ru.otus.hw.events.MongoAuthorCascadeDeleteListener;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@DisplayName("класс AuthorRepository")
@Import(MongoAuthorCascadeDeleteListener.class)
public class AuthorRepositoryWithListenersTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @DisplayName("При удалении автора если у него есть книга то должен бросить исключение")
    @Test
    void shouldThrowExceptionWhenDeleteAuthorWithBook() {

        Author author = Author.builder()
                .fullName("new_author").build();
        mongoOperations.save(author);

        Genre genre = Genre.builder()
                .name("new_genre").build();
        mongoOperations.save(genre);

        Book book = Book.builder()
                .title("new_book")
                .author(author)
                .genres(List.of(genre))
                .build();
        mongoOperations.save(book);

        assertThatThrownBy(() -> authorRepository.deleteById(author.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Author cannot be deleted as it is referenced by a book.");
    }

}
