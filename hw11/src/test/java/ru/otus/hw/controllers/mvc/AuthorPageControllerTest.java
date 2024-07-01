package ru.otus.hw.controllers.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс AuthorPageController")
@WebFluxTest(AuthorPageController.class)
public class AuthorPageControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("при запросе GET /authors - должна вернуться страница списка авторов")
    void testAuthorsListPage() {
        webTestClient.get().uri("/authors")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"authorsListPage\"");
                });
    }


    @Test
    @DisplayName("при запросе GET /authors/new - должна вернуться страница создания нового")
    void testNewAuthorPage() {
        webTestClient.get().uri("/authors/new")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"newAuthorPage\"");
                });
    }

    @Test
    @DisplayName("при запросе GET /authors/{id} - должна вернуться страница редактирования")
    void testAuthorPage() {
        webTestClient.get().uri("/authors/{id}", 1L)
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"editAuthorPage\"");
                });
    }

}
