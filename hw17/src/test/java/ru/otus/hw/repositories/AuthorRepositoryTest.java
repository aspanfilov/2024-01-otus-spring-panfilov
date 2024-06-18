package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.BaseContainerTest;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorRepositoryTest extends BaseContainerTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AuthorRepository repository;

    @DisplayName(" проверка сохранения и извлечения сущности author")
    @Test
    public void testAuthorPersistence() {
        var newAuthor = new Author(0, "new_author");
        var expectedAuthor = entityManager.persist(newAuthor);

        var actualAuthor = repository.findById(expectedAuthor.getId());

        assertThat(actualAuthor).isPresent()
                .get().usingRecursiveComparison().isEqualTo(expectedAuthor);
    }
}
