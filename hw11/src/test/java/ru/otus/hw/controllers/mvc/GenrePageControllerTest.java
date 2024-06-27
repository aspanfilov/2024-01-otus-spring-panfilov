package ru.otus.hw.controllers.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.mvc.GenrePageController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Класс GenrePageController")
@WebFluxTest(GenrePageController.class)
public class GenrePageControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("при запросе GET /genres - должна вернуться страница списка")
    void testGenresListPage() {
        webTestClient.get().uri("/genres")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"genresListPage\"");
                });
    }

    @Test
    @DisplayName("при запросе GET /genres/new - должна вернуться страница создания нового")
    void testNewGenrePage() {
        webTestClient.get().uri("/genres/new")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"newGenrePage\"");
                });
    }

    @Test
    @DisplayName("при запросе GET /genres/{id} - должна вернуться страница редактирования")
    void testGenrePage() {
        webTestClient.get().uri("/genres/{id}", 1L)
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
                .expectBody(String.class)
                .consumeWith(response -> {
                    assertThat(response.getResponseBody()).contains("id=\"editGenrePage\"");
                });
    }


}
