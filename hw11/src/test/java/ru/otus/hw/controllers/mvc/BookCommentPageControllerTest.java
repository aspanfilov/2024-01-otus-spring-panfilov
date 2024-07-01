package ru.otus.hw.controllers.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс BookCommentPageController")
@WebFluxTest(BookCommentPageController.class)
public class BookCommentPageControllerTest {

    private final static Long BOOK_ID = 1L;
    private final static Long BOOK_COMMENT_ID = 1L;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("при запросе GET /books/{bookId}/comments - должна вернуться страница списка")
    void testBookCommentsListPage() {
        webTestClient.get().uri("/books/{bookId}/comments", BOOK_ID)
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"bookCommentsListPage\"");
                });
    }

    @Test
    @DisplayName("при запросе GET /books/{bookId}/comments/new - должна вернуться страница создания нового")
    void testNewBookCommentPage() {
        webTestClient.get().uri("/books/{bookId}/comments/new", BOOK_ID)
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"newBookCommentPage\"");
                });
    }

    @Test
    @DisplayName("при запросе GET /books/{bookId}/comments/{bookCommentId} - должна вернуться страница редактирования")
    void testBookCommentPage() {
        webTestClient.get().uri("/books/{bookId}/comments/{bookCommentId}", BOOK_ID, BOOK_COMMENT_ID)
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"editBookCommentPage\"");
                });
    }

}
