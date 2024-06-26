package ru.otus.hw.controllers.mvc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Класс AuthorPageController")
@WebMvcTest(AuthorPageController.class)
public class AuthorPageControllerTest {

//    @Autowired
//    private MockMvc mvc;
//
//    @Test
//    @DisplayName("при запросе GET /authors - должна вернуться страница списка")
//    void testAuthorsListPage() throws Exception {
//        mvc.perform(get("/authors"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("authors/list"));
//    }
//
//    @Test
//    @DisplayName("при запросе GET /authors/new - должна вернуться страница создания нового")
//    void testNewAuthorPage() throws Exception {
//        mvc.perform(get("/authors/new"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("authors/new"));
//    }
//
//    @Test
//    @DisplayName("при запросе GET /authors/{id} - должна вернуться страница редактирования")
//    void testAuthorPage() throws Exception {
//        mvc.perform(get("/authors/1"))
//                .andExpect(status().isOk())
//                .andExpect(model().attribute("authorId", 1L))
//                .andExpect(view().name("authors/edit"));
//    }

}
