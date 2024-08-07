package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.BaseContainerTest;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с книгами")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest extends BaseContainerTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository repository;

    @DisplayName(" проверка сохранения и извлечения сущности book")
    @Test
    public void testBookPersistence() {
        var expectedBook = new Book(0, "new_book",
                new Author(0, "new_author"),
                List.of(new Genre(0, "new_genre")));

        entityManager.persist(expectedBook);

        var actualBook = repository.findById(expectedBook.getId());

        assertThat(actualBook).isPresent()
                .get().usingRecursiveComparison().isEqualTo(expectedBook);
    }

}


