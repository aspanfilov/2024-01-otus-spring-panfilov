package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с авторами")
@DataJpaTest
@Import({JpaAuthorRepository.class})
public class JdbcAuthorRepositoryTest {

    @Autowired
    private JpaAuthorRepository repositoryJpa;

    private List<Author> dbAuthors;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
    }

    @DisplayName("должен загружать автора по id")
    @ParameterizedTest
    @MethodSource("getDbAuthors")
    void shouldReturnCorrectAuthorById(Author expectedAuthor) {
        var actualAuthor = repositoryJpa.findById(expectedAuthor.getId());
        assertThat(actualAuthor).isPresent()
                .get()
                .isEqualTo(expectedAuthor);
    }

    @DisplayName("должен загружать список всех авторов")
    @Test
    void shouldReturnCorrectAuthorsList() {
        var actualAuthors = repositoryJpa.findAll();
        var expectedAuthors = dbAuthors;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
        actualAuthors.forEach(System.out::println);
    }

    @DisplayName("должен сохранять нового автора")
    @Test
    void shouldSaveNewAuthor() {
        var expectedAuthor = new Author(0, "newAuthor");
        var returnedAuthor = repositoryJpa.save(expectedAuthor);
        assertThat(returnedAuthor).isNotNull()
                .matches(author -> author.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedAuthor);

        assertThat(repositoryJpa.findById(returnedAuthor.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedAuthor);
    }

    @DisplayName("должен сохранять измененного автора")
    @Test
    void shouldSaveUpdatedAuthor() {
        var expectedAuthor = new Author(1L, "updatableAuthor");

        assertThat(repositoryJpa.findById(expectedAuthor.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedAuthor);

        var returnedAuthor = repositoryJpa.save(expectedAuthor);
        assertThat(returnedAuthor).isNotNull()
                .matches(author -> author.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedAuthor);

        assertThat(repositoryJpa.findById(returnedAuthor.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedAuthor);
    }

    @DisplayName("должен удалять автора по id ")
    @Test
    void shouldDeleteAuthor() {
        assertThat(repositoryJpa.findById(1L)).isPresent();
        repositoryJpa.deleteById(1L);
        assertThat(repositoryJpa.findById(1L)).isEmpty();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.rangeClosed(1, 3).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }
}
