package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;
import ru.otus.hw.AbstractDataResetTest;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий BookRepositoryCustom")
public class BookRepositoryCustomTest extends AbstractDataResetTest {

    @Autowired
    private BookRepositoryCustom bookRepository;

    @DisplayName("Проверка получения всех книг с детальной информацией")
    @Test
    void testFindAllBooks() {
        StepVerifier.create(bookRepository.findAll())
                .consumeNextWith(book -> {
                    assertThat(book.getId()).isEqualTo(1L);
                    assertThat(book.getTitle()).isEqualTo("BookTitle_1");
                    assertThat(book.getAuthor()).isEqualTo("Author_1");
                    assertThat(book.getGenres()).containsExactlyInAnyOrder("Genre_1", "Genre_2");
                })
                .consumeNextWith(book -> {
                    assertThat(book.getId()).isEqualTo(2L);
                    assertThat(book.getTitle()).isEqualTo("BookTitle_2");
                    assertThat(book.getAuthor()).isEqualTo("Author_2");
                    assertThat(book.getGenres()).containsExactlyInAnyOrder("Genre_3", "Genre_4");
                })
                .consumeNextWith(book -> {
                    assertThat(book.getId()).isEqualTo(3L);
                    assertThat(book.getTitle()).isEqualTo("BookTitle_3");
                    assertThat(book.getAuthor()).isEqualTo("Author_3");
                    assertThat(book.getGenres()).containsExactlyInAnyOrder("Genre_5", "Genre_6");
                })
                .expectComplete()
                .verify();
    }

    @DisplayName("Проверка получения книги по ID с полной информацией")
    @Test
    void testFindById() {
        Long expectedBookId = 1L;

        StepVerifier.create(bookRepository.findById(expectedBookId))
                .consumeNextWith(book -> {
                    assertThat(book.getId()).isEqualTo(expectedBookId);
                    assertThat(book.getTitle()).isEqualTo("BookTitle_1");
                    assertThat(book.getAuthor().getId()).isEqualTo(1L);
                    assertThat(book.getAuthor().getFullName()).isEqualTo("Author_1");
                    assertThat(book.getGenres()).hasSize(2)
                            .extracting(Genre::getName)
                            .containsExactlyInAnyOrder("Genre_1", "Genre_2");
                })
                .expectComplete()
                .verify();
    }

}
