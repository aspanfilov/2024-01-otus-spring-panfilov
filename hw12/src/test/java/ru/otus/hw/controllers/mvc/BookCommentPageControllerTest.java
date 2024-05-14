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

@DisplayName("Класс BookCommentPageController")
@WebMvcTest(BookCommentPageController.class)
public class BookCommentPageControllerTest {

    private final static Long BOOK_ID = 1L;
    private final static Long BOOK_COMMENT_ID = 1L;

    @Autowired
    private MockMvc mvc;

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /books/{bookId}/comments - должна вернуться страница списка")
    void testBookCommentsListPage() throws Exception {
        mvc.perform(get("/books/{bookId}/comments", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("books/comments/list"))
                .andExpect(model().attribute("bookId", BOOK_ID));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /books/{bookId}/comments/new - должна вернуться страница создания нового")
    void testNewBookCommentPage() throws Exception {
        mvc.perform(get("/books/{bookId}/comments/new", BOOK_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("books/comments/new"))
                .andExpect(model().attribute("bookId", BOOK_ID));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /books/{bookId}/comments/{bookCommentId} - должна вернуться страница редактирования")
    void testBookCommentPage() throws Exception {
        mvc.perform(get("/books/{bookId}/comments/{bookCommentId}", BOOK_ID, BOOK_COMMENT_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("books/comments/edit"))
                .andExpect(model().attribute("bookId", BOOK_ID))
                .andExpect(model().attribute("bookCommentId", BOOK_COMMENT_ID));
    }

    @Test
    @DisplayName("при запросе GET /books/{bookId}/comments без аутентификации - должен вернуть 401")
    void testBookCommentsListPageUnauthorized() throws Exception {
        mvc.perform(get("/books/{bookId}/comments", BOOK_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе GET /books/{bookId}/comments/new без аутентификации - должен вернуть 401")
    void testNewBookCommentPageUnauthorized() throws Exception {
        mvc.perform(get("/books/{bookId}/comments/new", BOOK_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе GET /books/{bookId}/comments/{bookCommentId} без аутентификации - должен вернуть 401")
    void testBookCommentPageUnauthorized() throws Exception {
        mvc.perform(get("/books/{bookId}/comments/{bookCommentId}", BOOK_ID, BOOK_COMMENT_ID))
                .andExpect(status().isUnauthorized());
    }

}
