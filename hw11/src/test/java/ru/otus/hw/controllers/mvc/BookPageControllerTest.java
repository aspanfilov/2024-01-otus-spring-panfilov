package ru.otus.hw.controllers.mvc;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@DisplayName("Класс BookPageController")
@WebMvcTest(BookPageController.class)
public class BookPageControllerTest {

//    private final static Long BOOK_ID = 1L;
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private AuthorService authorService;
//
//    @MockBean
//    private GenreService genreService;
//
//    @BeforeEach
//    void setUp() {
//        when(authorService.findAll()).thenReturn(Collections.emptyList());
//        when(genreService.findAll()).thenReturn(Collections.emptyList());
//    }
//
//    @Test
//    @DisplayName("при запросе GET /books - должна вернуться страница списка")
//    void testBooksListPage() throws Exception {
//        mvc.perform(get("/books"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("books/list"));
//    }
//
//    @Test
//    @DisplayName("при запросе GET /books/new - должна вернуться страница создания нового")
//    void testNewAuthorPage() throws Exception {
//        mvc.perform(get("/books/new"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("books/new"))
//                .andExpect(model().attributeExists("authors"))
//                .andExpect(model().attributeExists("genres"));
//    }
//
//    @Test
//    @DisplayName("при запросе GET /books/{id} - должна вернуться страница редактирования")
//    void testAuthorPage() throws Exception {
//        mvc.perform(get("/books/{id}", BOOK_ID))
//                .andExpect(status().isOk())
//                .andExpect(view().name("books/edit"))
//                .andExpect(model().attribute("bookId", BOOK_ID))
//                .andExpect(model().attributeExists("authors"))
//                .andExpect(model().attributeExists("genres"));
//    }
}
