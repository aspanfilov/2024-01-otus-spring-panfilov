package ru.otus.hw.repositories;

import lombok.val;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
@Import({JpaAuthorRepository.class})
public class JpaAuthorRepositoryTest {

    @Autowired
    private JpaAuthorRepository repositoryJpa;

    @Autowired
    private TestEntityManager entityManager;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName(" должен загружать автора по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldFindExpectedAuthorById(Author expectedAuthor) {
        val optionalActualAuthor = repositoryJpa.findById(expectedAuthor.getId());
        assertThat(optionalActualAuthor).isPresent()
                .get().usingRecursiveComparison().isEqualTo(expectedAuthor);
    }

    @DisplayName(" должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        val actualAuthors = repositoryJpa.findAll();
        val expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }

    @DisplayName(" должен сохранять нового автора")
    @Test
    void shouldSaveNewAuthor() {
        val author = new Author(0, "newAuthor");

        repositoryJpa.save(author);
        assertThat(author.getId()).isGreaterThan(0);

        val actualAuthor = entityManager.find(Author.class, author.getId());
        assertThat(actualAuthor).isNotNull()
                .usingRecursiveComparison().isEqualTo(author);
    }

    @DisplayName(" должен сохранять измененного автора")
    @Test
    void shouldSaveUpdatedAuthor() {
        var expectedAuthor = new Author(1, "updatableAuthor");

        assertThat(entityManager.find(Author.class, expectedAuthor.getId()))
                .isNotEqualTo(expectedAuthor);

        var returnedAuthor = repositoryJpa.save(expectedAuthor);
        assertThat(returnedAuthor).isNotNull()
                .matches(author -> author.getId() > 0)
                .usingRecursiveComparison().isEqualTo(expectedAuthor);

        assertThat(entityManager.find(Author.class, returnedAuthor.getId()))
                .isEqualTo(returnedAuthor);
    }

    @DisplayName("должен удалять автора по id ")
    @Test
    void shouldDeleteAuthor() {
        assertThat(entityManager.find(Author.class, 1)).isNotNull();
        repositoryJpa.deleteById(1);
        assertThat(entityManager.find(Author.class, 1)).isNull();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.rangeClosed(1, 3).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }
}
