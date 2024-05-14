package ru.otus.hw.controllers.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Класс GenrePageController")
@WebMvcTest(GenrePageController.class)
public class GenrePageControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /genres - должна вернуться страница списка")
    void testGenresListPage() throws Exception {
        mvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres/list"));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /genres/new - должна вернуться страница создания нового")
    void testNewGenrePage() throws Exception {
        mvc.perform(get("/genres/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres/new"));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /genres/{id} - должна вернуться страница редактирования")
    void testGenrePage() throws Exception {
        mvc.perform(get("/genres/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genreId", 1L))
                .andExpect(view().name("genres/edit"));
    }

    @Test
    @DisplayName("при запросе GET /genres без аутентификации - должен вернуть 401")
    void testGenresListPageUnauthorized() throws Exception {
        mvc.perform(get("/genres"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе GET /genres/new без аутентификации - должен вернуть 401")
    void testNewGenrePageUnauthorized() throws Exception {
        mvc.perform(get("/genres/new"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе GET /genres/{id} без аутентификации - должен вернуть 401")
    void testGenrePageUnauthorized() throws Exception {
        mvc.perform(get("/genres/1"))
                .andExpect(status().isUnauthorized());
    }


}
