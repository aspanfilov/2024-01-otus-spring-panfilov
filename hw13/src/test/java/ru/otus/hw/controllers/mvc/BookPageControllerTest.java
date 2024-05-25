package ru.otus.hw.controllers.mvc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.GenreService;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Класс BookPageController")
@WebMvcTest(BookPageController.class)
@WithMockUser
public class BookPageControllerTest {

    private final static Long BOOK_ID = 1L;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @BeforeEach
    void setUp() {
        when(authorService.findAll()).thenReturn(Collections.emptyList());
        when(genreService.findAll()).thenReturn(Collections.emptyList());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /books - должна вернуться страница списка")
    void testBooksListPage() throws Exception {
        mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /books/new - должна вернуться страница создания нового")
    void testNewAuthorPage() throws Exception {
        mvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/new"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /books/{id} - должна вернуться страница редактирования")
    void testAuthorPage() throws Exception {
        mvc.perform(get("/books/{id}", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("books/edit"))
                .andExpect(model().attribute("bookId", BOOK_ID))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"));
    }
}
