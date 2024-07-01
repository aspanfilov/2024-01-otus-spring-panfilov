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
import ru.otus.hw.dtos.BookCommentDTO;
import ru.otus.hw.models.BookComment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс BookCommentController")
public class BookCommentControllerTest extends AbstractDataResetTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Test
    @DisplayName("при запросе GET /api/v1/books/{bookId}/comments - должен вернуться список комментариев")
    void testGetBookComments() {
        webTestClient.get().uri("/api/v1/books/{bookId}/comments", 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(BookComment.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(comment -> {
                    assertThat(comment).isNotNull();
                    assertThat(comment.getCommentText()).isEqualTo("comment_1");
                })
                .consumeNextWith(comment -> {
                    assertThat(comment).isNotNull();
                    assertThat(comment.getCommentText()).isEqualTo("comment_2");
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе GET /api/v1/books/{bookid}/comments/{bookCommentId} - должен вернуть комментарий")
    void testGetBookComment() {
        webTestClient.get().uri("/api/v1/books/{bookId}/comments/{bookCommentId}", 1, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(BookComment.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(comment -> {
                    assertThat(comment).isNotNull();
                    assertThat(comment.getId()).isEqualTo(1L);
                    assertThat(comment.getCommentText()).isEqualTo("comment_1");
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе GET /api/v1/books/{bookid}/comments/{bookCommentId} с несуществующим ID - должен вернуть 404")
    void testGetBookComment_withNonExistentId() {
        webTestClient.get().uri("/api/v1/books/{bookId}/comments/{bookCommentId}", 1, 123)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("при запросе POST /api/v1/books/{bookId}/comments - должен создать новый комментарий")
    void testCreateBookComment() {
        BookCommentDTO newCommentDTO = BookCommentDTO.builder()
                .id(null)
                .commentText("New Comment")
                .bookId(1L)
                .build();

        webTestClient.post().uri("/api/v1/books/{bookId}/comments", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCommentDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(BookComment.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(expectedComment -> {
                    assertThat(expectedComment).isNotNull();
                    assertThat(expectedComment.getCommentText()).isEqualTo("New Comment");

                    r2dbcEntityTemplate
                            .selectOne(Query.query(Criteria.where("id").is(expectedComment.getId())), BookComment.class)
                            .as(StepVerifier::create)
                            .consumeNextWith(actualComment -> {
                                assertThat(actualComment).isNotNull();
                                assertThat(actualComment.getId()).isEqualTo(expectedComment.getId());
                                assertThat(actualComment.getCommentText()).isEqualTo("New Comment");
                                assertThat(actualComment.getBookId()).isEqualTo(1L);
                            })
                            .verifyComplete();
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе POST /api/v1/books/{bookId}/comments с некорректными данными - должен вернуть ошибку")
    void testCreateBookComment_withValidationErrors() {
        BookCommentDTO invalidComment = BookCommentDTO.builder()
                .id(null)
                .commentText("")
                .build();

        webTestClient.post().uri("/api/v1/books/{bookId}/comments", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidComment)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("при запросе PUT /api/v1/books/{bookId}/comments/{bookCommentId} - должен обновить комментарий")
    void testUpdateBookComment() {
        BookCommentDTO updatedComment = new BookCommentDTO(1L, "Updated Comment",1L);

        webTestClient.put().uri("/api/v1/books/{bookId}/comments/{bookCommentId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedComment)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(BookComment.class)
                .getResponseBody()
                .as(StepVerifier::create)
                .consumeNextWith(comment -> {
                    assertThat(comment).isNotNull();
                    assertThat(comment.getId()).isEqualTo(1L);
                    assertThat(comment.getCommentText()).isEqualTo("Updated Comment");

                    r2dbcEntityTemplate
                            .selectOne(Query.query(Criteria.where("id").is(1)), BookComment.class)
                            .as(StepVerifier::create)
                            .consumeNextWith(actualComment -> {
                                assertThat(actualComment).isNotNull();
                                assertThat(actualComment.getId()).isEqualTo(1L);
                                assertThat(actualComment.getCommentText()).isEqualTo("Updated Comment");
                                assertThat(actualComment.getBookId()).isEqualTo(1L);
                            })
                            .verifyComplete();
                })
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("при запросе PUT /api/v1/books/{bookId}/comments/{bookCommentId} с некорректными данными - должен вернуть статус ошибки")
    void testUpdateBookComment_withValidationErrors() {
        BookCommentDTO invalidComment = new BookCommentDTO(1L, "", 1L);

        webTestClient.put().uri("/api/v1/books/{bookId}/comments/{bookCommentId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidComment)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("при запросе DELETE /api/v1/books/{bookId}/comments/{bookCommentId} - должен удалить комментарий")
    void testDeleteBookComment() {
        webTestClient.delete().uri("/api/v1/books/{bookId}/comments/{bookCommentId}", 1, 1)
                .exchange()
                .expectStatus().isNoContent();

        r2dbcEntityTemplate
                .selectOne(Query.query(Criteria.where("id").is(1)), BookComment.class)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

}
