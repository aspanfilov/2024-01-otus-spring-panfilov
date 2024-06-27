package ru.otus.hw.controllers.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import ru.otus.hw.AbstractDataResetTest;
import ru.otus.hw.config.ObjectMapperConfig;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.models.Author;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс AuthorController")
public class AuthorControllerTest extends AbstractDataResetTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Test
    @DisplayName("при запросе GET /api/v1/authors - должен вернуться список авторов")
    void testGetAuthors() {
        webTestClient.get().uri("/api/v1/authors")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Author.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .expectNextMatches(author -> author.getFullName().equals("Author_1"))
                .expectNextMatches(author -> author.getFullName().equals("Author_2"))
                .expectNextMatches(author -> author.getFullName().equals("Author_3"))
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе GET /api/v1/authors/{id} - должен вернуть автора")
    void testGetAuthor() {
        webTestClient.get().uri("/api/v1/authors/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Author.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(author -> {
                    assertThat(author).isNotNull();
                    assertThat(author.getId()).isEqualTo(1L);
                    assertThat(author.getFullName()).isEqualTo("Author_1");
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе GET /api/v1/authors/{id} с несуществующим ID - должен вернуть 404")
    void testGetAuthorByNonExistentId() {
        webTestClient.get().uri("/api/v1/authors/{id}", 123)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("при запросе POST /api/v1/authors - должен создать нового автора")
    void testCreateAuthor() {
        AuthorDTO newAuthor = new AuthorDTO(null, "New Author");

        webTestClient.post().uri("/api/v1/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newAuthor)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Author.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(expectedAuthor -> {
                    assertThat(expectedAuthor).isNotNull();
                    assertThat(expectedAuthor.getFullName()).isEqualTo("New Author");

                    r2dbcEntityTemplate
                            .selectOne(Query.query(Criteria.where("id").is(expectedAuthor.getId())), Author.class)
                            .as(StepVerifier::create)
                            .consumeNextWith(actualAuthor -> {
                                assertThat(actualAuthor).isNotNull();
                                assertThat(actualAuthor.getId()).isEqualTo(expectedAuthor.getId());
                                assertThat(actualAuthor.getFullName()).isEqualTo("New Author");
                            })
                            .verifyComplete();
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе PUT /api/v1/authors/{id} - должен обновить автора")
    void testUpdateAuthor() {
        AuthorDTO updatedAuthor = new AuthorDTO(1L, "Updated Author");

        webTestClient.put().uri("/api/v1/authors/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedAuthor)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Author.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(author -> {
                    assertThat(author).isNotNull();
                    assertThat(author.getId()).isEqualTo(1);
                    assertThat(author.getFullName()).isEqualTo("Updated Author");
                })
                .expectComplete()
                .verify();

        r2dbcEntityTemplate
                .selectOne(Query.query(Criteria.where("id").is(1)), Author.class)
                .as(StepVerifier::create)
                .consumeNextWith(author -> {
                    assertThat(author).isNotNull();
                    assertThat(author.getId()).isEqualTo(1);
                    assertThat(author.getFullName()).isEqualTo("Updated Author");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("при запросе DELETE /api/v1/authors/{id} - должен удалить автора")
    void testDeleteAuthor() {
        webTestClient.delete().uri("/api/v1/authors/{id}", 1)
                .exchange()
                .expectStatus().isNoContent();

        r2dbcEntityTemplate.selectOne(Query.query(Criteria.where("id").is(1)), Author.class)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

}
