package ru.otus.hw.controllers.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import ru.otus.hw.AbstractDataResetTest;
import ru.otus.hw.dtos.book.BookBasicDto;
import ru.otus.hw.dtos.book.BookDetailDTO;
import ru.otus.hw.dtos.book.BookReferenceDTO;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookGenreRef;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс BookController")
public class BookControllerTest extends AbstractDataResetTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Test
    @DisplayName("при запросе GET /api/v1/books - должен вернуться список книг")
    void testGetBooks() {
        webTestClient.get().uri("/api/v1/books")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(BookDetailDTO.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextMatches(book -> book.getTitle().equals("BookTitle_1"))
                .expectNextMatches(book -> book.getTitle().equals("BookTitle_2"))
                .expectNextMatches(book -> book.getTitle().equals("BookTitle_3"))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе GET /api/v1/books/{id} - должен вернуть книгу")
    void testGetBook() {
        webTestClient.get().uri("/api/v1/books/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Book.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(book -> {
                    assertThat(book).isNotNull();
                    assertThat(book.getId()).isEqualTo(1L);
                    assertThat(book.getTitle()).isEqualTo("BookTitle_1");
                    assertThat(book.getAuthorId()).isEqualTo(1L);
                    assertThat(book.getAuthor()).isNotNull();
                    assertThat(book.getAuthor().getId()).isEqualTo(1L);
                    assertThat(book.getAuthor().getFullName()).isEqualTo("Author_1");
                    assertThat(book.getGenres()).isNotNull().hasSize(2);
                    assertThat(book.getGenres()).extracting("id").containsExactlyInAnyOrder(1L, 2L);
                    assertThat(book.getGenres()).extracting("name").containsExactlyInAnyOrder("Genre_1", "Genre_2");
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе GET /api/v1/books/{id} с несуществующим ID - должен вернуть 404")
    void testGetBookByNonExistentId() {
        webTestClient.get().uri("/api/v1/books/{id}", 123)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("при запросе POST /api/v1/books - должен создать новую книгу")
    void testCreateBook() {
        BookReferenceDTO newBook = new BookReferenceDTO(null, "New Book", 1L, Set.of(1L, 2L));

        webTestClient.post().uri("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newBook)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(BookBasicDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(expectedBook -> {
                    assertThat(expectedBook).isNotNull();
                    assertThat(expectedBook.getTitle()).isEqualTo("New Book");

                    r2dbcEntityTemplate
                            .selectOne(Query.query(Criteria.where("id").is(expectedBook.getId())), Book.class)
                            .as(StepVerifier::create)
                            .consumeNextWith(actualBook -> {
                                assertThat(actualBook).isNotNull();
                                assertThat(actualBook.getId()).isEqualTo(expectedBook.getId());
                                assertThat(actualBook.getTitle()).isEqualTo("New Book");
                                assertThat(actualBook.getAuthorId()).isEqualTo(1L);
                            })
                            .verifyComplete();
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе PUT /api/v1/books/{id} - должен обновить книгу")
    void testUpdateBook() {
        BookReferenceDTO updatedBook = new BookReferenceDTO(1L, "Updated Book", 2L, Set.of(3L, 4L));

        webTestClient.put().uri("/api/v1/books/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedBook)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(BookBasicDto.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(book -> {
                    assertThat(book).isNotNull();
                    assertThat(book.getId()).isEqualTo(1);
                    assertThat(book.getTitle()).isEqualTo("Updated Book");
                })
                .expectComplete()
                .verify();

        r2dbcEntityTemplate
                .selectOne(Query.query(Criteria.where("id").is(1)), Book.class)
                .as(StepVerifier::create)
                .consumeNextWith(book -> {
                    assertThat(book).isNotNull();
                    assertThat(book.getId()).isEqualTo(1);
                    assertThat(book.getTitle()).isEqualTo("Updated Book");
                    assertThat(book.getAuthorId()).isEqualTo(2);
                })
                .verifyComplete();

        r2dbcEntityTemplate
                .select(Query.query(Criteria.where("book_id").is(1)), BookGenreRef.class)
                .collectList()
                .as(StepVerifier::create)
                .consumeNextWith(genreRefs -> {
                    assertThat(genreRefs).hasSize(2);
                    assertThat(genreRefs).extracting("genreId").containsExactlyInAnyOrder(3L, 4L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("при запросе DELETE /api/v1/books/{id} - должен удалить книгу")
    void testDeleteBook() {
        webTestClient.delete().uri("/api/v1/books/{id}", 1)
                .exchange()
                .expectStatus().isNoContent();

        r2dbcEntityTemplate.selectOne(Query.query(Criteria.where("id").is(1)), Book.class)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

}
