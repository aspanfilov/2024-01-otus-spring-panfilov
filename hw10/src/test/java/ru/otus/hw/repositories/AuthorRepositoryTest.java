package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
public class AuthorRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository repository;

    @DisplayName(" проверка сохранения и извлечения сущности author")
    @Test
    public void testAuthorPersistence() {
        var expectedAuthor = new Author(0, "new_author");
        entityManager.persist(expectedAuthor);

        var actualAuthor = repository.findById(expectedAuthor.getId());

        assertThat(actualAuthor).isPresent()
                .get().usingRecursiveComparison().isEqualTo(expectedAuthor);
    }
}
