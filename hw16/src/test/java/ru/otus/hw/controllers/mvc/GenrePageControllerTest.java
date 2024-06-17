package ru.otus.hw.controllers.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controllers.mvc.GenrePageController;

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
    @DisplayName("при запросе GET /genres - должна вернуться страница списка")
    void testGenresListPage() throws Exception {
        mvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres/list"));
    }

    @Test
    @DisplayName("при запросе GET /genres/new - должна вернуться страница создания нового")
    void testNewGenrePage() throws Exception {
        mvc.perform(get("/genres/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres/new"));
    }

    @Test
    @DisplayName("при запросе GET /genres/{id} - должна вернуться страница редактирования")
    void testGenrePage() throws Exception {
        mvc.perform(get("/genres/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genreId", 1L))
                .andExpect(view().name("genres/edit"));
    }


}
