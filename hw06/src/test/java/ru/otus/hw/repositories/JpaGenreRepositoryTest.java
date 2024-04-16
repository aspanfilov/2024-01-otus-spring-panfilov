package ru.otus.hw.repositories;

import lombok.val;
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
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами")
@DataJpaTest
@Import({JpaGenreRepository.class})
public class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository repositoryJpa;

    @Autowired
    private TestEntityManager entityManager;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("должен загружать жанр по id")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void shouldReturnCorrectGenreById(Genre expectedGenre) {
        val optionalActualGenre = repositoryJpa.findById(expectedGenre.getId());
        assertThat(optionalActualGenre).isPresent().get()
                .usingRecursiveComparison().isEqualTo(expectedGenre);
    }

    @DisplayName("должен загружать жанр по списку id")
    @Test
    void shouldReturnCorrectGenreByIds() {
        val expectedGenre = getDbGenres().get(0);
        val actualGenres = repositoryJpa.findAllByIds(Set.of(expectedGenre.getId()));
        assertThat(actualGenres)
                .containsOnly(expectedGenre);
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        val actualGenres = repositoryJpa.findAll();
        val expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
    }

    @DisplayName("должен сохранять новый жанр")
    @Test
    void shouldSaveNewGenre() {
        val genre = new Genre(0, "newGenre");

        repositoryJpa.save(genre);
        assertThat(genre.getId()).isGreaterThan(0);

        val actualGenre = entityManager.find(Genre.class, genre.getId());
        assertThat(actualGenre).isNotNull()
                .usingRecursiveComparison().isEqualTo(genre);
    }

    @DisplayName("должен сохранять измененный жанр")
    @Test
    void shouldSaveUpdatedGenre() {
        var expectedGenre = new Genre(1L, "updatableGenre");

        assertThat(entityManager.find(Genre.class, expectedGenre.getId()))
                .isNotEqualTo(expectedGenre);

        var returnedGenre = repositoryJpa.save(expectedGenre);
        assertThat(returnedGenre).isNotNull()
                .matches(author -> author.getId() > 0)
                .usingRecursiveComparison().isEqualTo(expectedGenre);

        assertThat(entityManager.find(Genre.class, returnedGenre.getId()))
                .isEqualTo(returnedGenre);
    }

    @DisplayName("должен удалять жанр по id ")
    @Test
    void shouldDeleteGenre() {
        assertThat(entityManager.find(Genre.class, 1)).isNotNull();
        repositoryJpa.deleteById(1);
        assertThat(entityManager.find(Genre.class, 1)).isNull();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.rangeClosed(1, 6).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}
