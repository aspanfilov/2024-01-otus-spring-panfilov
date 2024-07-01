package ru.otus.hw.controllers.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс BookPageController")
@WebFluxTest(BookPageController.class)
public class BookPageControllerTest {

    private final static Long BOOK_ID = 1L;

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Test
    @DisplayName("при запросе GET /books - должна вернуться страница списка")
    void testBooksListPage() {
        webTestClient.get().uri("/books")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"booksListPage\"");
                });
    }

    @Test
    @DisplayName("при запросе GET /books/new - должна вернуться страница создания нового")
    void testNewAuthorPage() {
        webTestClient.get().uri("/books/new")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"newBookPage\"");
                });
    }

    @Test
    @DisplayName("при запросе GET /books/{id} - должна вернуться страница редактирования")
    void testAuthorPage() {
        webTestClient.get().uri("/books/{id}", BOOK_ID)
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"editBookPage\"");
                });
    }
}
