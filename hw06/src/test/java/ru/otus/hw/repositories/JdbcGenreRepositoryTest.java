package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с жанрами")
@DataJpaTest
@Import({JpaGenreRepository.class})
public class JdbcGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository repositoryJdbc;

    private List<Genre> dbGenres;

    @BeforeEach
    void setUp() {
        dbGenres = getDbGenres();
    }

    @DisplayName("должен загружать жанр по id")
    @ParameterizedTest
    @MethodSource("getDbGenres")
    void shouldReturnCorrectGenreById(Genre expectedGenre) {
        var actualGenre = repositoryJdbc.findById(expectedGenre.getId());
        assertThat(actualGenre).isPresent()
                .get()
                .isEqualTo(expectedGenre);
    }

    @DisplayName("должен загружать жанр по списку id")
    @Test
    void shouldReturnCorrectGenreByIds() {
        var expectedGenre = getDbGenres().get(0);
        var actualGenres = repositoryJdbc.findAllByIds(Set.of(expectedGenre.getId()));
        assertThat(actualGenres)
                .containsOnly(expectedGenre);

    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        var actualGenres = repositoryJdbc.findAll();
        var expectedGenres = dbGenres;

        assertThat(actualGenres).containsExactlyElementsOf(expectedGenres);
        actualGenres.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новый жанр")
    @Test
    void shouldSaveNewGenre() {
        var expectedGenres = new Genre(0, "newGenre");
        var returnedGenres = repositoryJdbc.save(expectedGenres);
        assertThat(returnedGenres).isNotNull()
                .matches(genre -> genre.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedGenres);

        assertThat(repositoryJdbc.findById(returnedGenres.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedGenres);
    }

    @DisplayName("должен сохранять измененный жанр")
    @Test
    void shouldSaveUpdatedGenre() {
        var expectecGenre = new Genre(1L, "updatableGenre");

        assertThat(repositoryJdbc.findById(expectecGenre.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectecGenre);

        var returnedGenre = repositoryJdbc.save(expectecGenre);
        assertThat(returnedGenre).isNotNull()
                .matches(genre -> genre.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectecGenre);

        assertThat(repositoryJdbc.findById(returnedGenre.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedGenre);
    }

    @DisplayName("должен удалять жанр по id ")
    @Test
    void shouldDeleteGenre() {
        assertThat(repositoryJdbc.findById(1L)).isPresent();
        repositoryJdbc.deleteById(1L);
        assertThat(repositoryJdbc.findById(1L)).isEmpty();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.rangeClosed(1, 6).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }
}
