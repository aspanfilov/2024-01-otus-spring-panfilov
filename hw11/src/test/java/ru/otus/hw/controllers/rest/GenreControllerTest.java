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
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.models.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс GenreController")
public class GenreControllerTest extends AbstractDataResetTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Test
    @DisplayName("при запросе GET /api/v1/genres - должен вернуться список dto жанров")
    void testGetGenres() {
        webTestClient.get().uri("/api/v1/genres")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Genre.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextMatches(genre -> genre.getName().equals("Genre_1"))
                .expectNextMatches(genre -> genre.getName().equals("Genre_2"))
                .expectNextMatches(genre -> genre.getName().equals("Genre_3"))
                .expectNextMatches(genre -> genre.getName().equals("Genre_4"))
                .expectNextMatches(genre -> genre.getName().equals("Genre_5"))
                .expectNextMatches(genre -> genre.getName().equals("Genre_6"))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе GET /api/v1/genres/{id} - должен вернуть dto жанра")
    void testGetGenre() {
        webTestClient.get().uri("/api/v1/genres/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Genre.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(genre -> {
                    assertThat(genre).isNotNull();
                    assertThat(genre.getId()).isEqualTo(1L);
                    assertThat(genre.getName()).isEqualTo("Genre_1");
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе GET /api/v1/genres/{id} с несуществующим ID - должен вернуть 404")
    void testGetGenreByNonExistentId() {
        webTestClient.get().uri("/api/v1/genres/{id}", 123)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("при запросе POST /api/v1/genres - должен создать новый жанр")
    void testCreateGenre() {
        GenreDTO newGenre = new GenreDTO(null, "New Genre");

        webTestClient.post().uri("/api/v1/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newGenre)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Genre.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(expectedGenre -> {
                    assertThat(expectedGenre).isNotNull();
                    assertThat(expectedGenre.getName()).isEqualTo("New Genre");

                    r2dbcEntityTemplate
                            .selectOne(Query.query(Criteria.where("id").is(expectedGenre.getId())), Genre.class)
                            .as(StepVerifier::create)
                            .consumeNextWith(actualGenre -> {
                                assertThat(actualGenre).isNotNull();
                                assertThat(actualGenre.getId()).isEqualTo(expectedGenre.getId());
                                assertThat(actualGenre.getName()).isEqualTo("New Genre");
                            })
                            .verifyComplete();
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе PUT /api/v1/genres/{id} - должен обновить жанр")
    void testUpdateGenre() {
        GenreDTO updatedGenre = new GenreDTO(1L, "Updated Genre");

        webTestClient.put().uri("/api/v1/genres/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedGenre)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Genre.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(genre -> {
                    assertThat(genre).isNotNull();
                    assertThat(genre.getId()).isEqualTo(1);
                    assertThat(genre.getName()).isEqualTo("Updated Genre");
                })
                .expectComplete()
                .verify();

        r2dbcEntityTemplate
                .selectOne(Query.query(Criteria.where("id").is(1)), Genre.class)
                .as(StepVerifier::create)
                .consumeNextWith(genre -> {
                    assertThat(genre).isNotNull();
                    assertThat(genre.getId()).isEqualTo(1);
                    assertThat(genre.getName()).isEqualTo("Updated Genre");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("при запросе DELETE /api/v1/genres/{id} - должен удалить жанр")
    void testDeleteGenre() {
        webTestClient.delete().uri("/api/v1/genres/{id}", 1)
                .exchange()
                .expectStatus().isNoContent();

        r2dbcEntityTemplate.selectOne(Query.query(Criteria.where("id").is(1)), Genre.class)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

}
