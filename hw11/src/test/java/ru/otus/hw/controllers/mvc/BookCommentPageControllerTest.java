package ru.otus.hw.controllers.mvc;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@DisplayName("Класс BookCommentPageController")
@WebMvcTest(BookCommentPageController.class)
public class BookCommentPageControllerTest {

//    private final static Long BOOK_ID = 1L;
//    private final static Long BOOK_COMMENT_ID = 1L;
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Test
//    @DisplayName("при запросе GET /books/{bookId}/comments - должна вернуться страница списка")
//    void testBookCommentsListPage() throws Exception {
//        mvc.perform(get("/books/{bookId}/comments", BOOK_ID))
//                .andExpect(status().isOk())
//                .andExpect(view().name("books/comments/list"))
//                .andExpect(model().attribute("bookId", BOOK_ID));
//    }
//
//    @Test
//    @DisplayName("при запросе GET /books/{bookId}/comments/new - должна вернуться страница создания нового")
//    void testNewBookCommentPage() throws Exception {
//        mvc.perform(get("/books/{bookId}/comments/new", BOOK_ID))
//                .andExpect(status().isOk())
//                .andExpect(view().name("books/comments/new"))
//                .andExpect(model().attribute("bookId", BOOK_ID));
//    }
//
//    @Test
//    @DisplayName("при запросе GET /books/{bookId}/comments/{bookCommentId} - должна вернуться страница редактирования")
//    void testBookCommentPage() throws Exception {
//        mvc.perform(get("/books/{bookId}/comments/{bookCommentId}", BOOK_ID, BOOK_COMMENT_ID))
//                .andExpect(status().isOk())
//                .andExpect(view().name("books/comments/edit"))
//                .andExpect(model().attribute("bookId", BOOK_ID))
//                .andExpect(model().attribute("bookCommentId", BOOK_COMMENT_ID));
//    }

}
