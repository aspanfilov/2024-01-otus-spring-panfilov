package ru.otus.hw.healthcheck;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import ru.otus.hw.config.LibraryHealthIndicator;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import static java.util.Map.entry;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Класс LibraryHealthIndicator")
@ExtendWith(MockitoExtension.class)
public class LibraryHealthIndicatorTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GenreRepository genreRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LibraryHealthIndicator libraryHealthIndicator;

    @DisplayName("должен вернуть статус UP, если в таблицах есть хотя бы одна запись")
    @Test
    void shouldReturnStatusUpWhenAllTablesHaveRecords() {
        when(authorRepository.count()).thenReturn(10L);
        when(genreRepository.count()).thenReturn(5L);
        when(bookRepository.count()).thenReturn(20L);

        Health health = libraryHealthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).contains(
                entry("Authors", 10L),
                entry("Genres", 5L),
                entry("Books", 20L)
        );
    }

    @DisplayName("должен вернуть статус DOWN, если хотя бы одна таблица не имеет записей")
    @Test
    void shouldReturnStatusDownWhenAnyTableHasNoRecords() {
        when(authorRepository.count()).thenReturn(0L);
        when(genreRepository.count()).thenReturn(5L);
        when(bookRepository.count()).thenReturn(20L);

        Health health = libraryHealthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).contains(
                entry("Error", "No authors found"),
                entry("Genres", 5L),
                entry("Books", 20L));

    }
}
