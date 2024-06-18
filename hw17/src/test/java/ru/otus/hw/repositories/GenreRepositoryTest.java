package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.BaseContainerTest;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами")
@DataJpaTest()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GenreRepositoryTest extends BaseContainerTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GenreRepository repository;

    @DisplayName(" проверка сохранения и извлечения сущности genre")
    @Test
    public void testGenrePersistence() {
        var expectedGenre = new Genre(0, "new_genre");
        entityManager.persist(expectedGenre);

        var actualGenre = repository.findById(expectedGenre.getId());

        assertThat(actualGenre).isPresent()
                .get().usingRecursiveComparison().isEqualTo(expectedGenre);
    }
}

