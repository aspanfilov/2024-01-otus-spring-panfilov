package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.BookRequestDTO;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Класс BookController")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    private static final String BOOK_NOT_FOUND = "Book with id 123 not found";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private BookService bookService;

    private Author author;
    private List<Author> authors;

    private Genre genre;
    private List<Genre> genres;

    private Book book;
    private BookDTO bookDTO;
    private BookRequestDTO bookRequest;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "author1");
        authors = List.of(author);
        var authorDTO = new AuthorDTO(1L, "author1");

        genre = new Genre(1L, "genre1");
        genres = List.of(genre);
        var genreDTO = new GenreDTO(1L, "genre1");

        book = new Book(1L, "book1", author, List.of(genre));
        bookRequest = new BookRequestDTO(1L, "book1", 1L, Set.of(1L));
        bookDTO = BookDTO.builder()
                .id(1L)
                .title("book1")
                .author(authorDTO)
                .genres(List.of(genreDTO))
                .build();

        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);
    }

    @DisplayName("при запросе GET /books - должен вернуть список книг")
    @Test
    void testListBooks() throws Exception {
        List<BookDTO> bookDTOs = List.of(bookDTO);

        when(bookService.findAll()).thenReturn(bookDTOs);

            mvc.perform(get("/books"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("books/list"))
                    .andExpect(model().attribute("books", bookDTOs));
    }

    @DisplayName("при запросе GET /books/new - возвращается страница создания новой книги")
    @Test
    void testNewBook() throws Exception {
        mvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/new"))
                .andExpect(model().attributeExists("bookRequest"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("genres"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres));
    }

    @DisplayName("при запросе POST /books - должен создать новую книгу и перенаправить на список книг")
    @Test
    void testCreateBook() throws Exception {
        mvc.perform(post("/books")
                        .param("title", "book1")
                        .param("authorId", "1")
                        .param("genreIds", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).insert("book1", 1L, Set.of(1L));
    }

    @DisplayName("При запросе POST /books с некорректными данными - должен вернуться к странице создания книги")
    @Test
    void testCreateBook_withValidationErrors() throws Exception {
        mvc.perform(post("/books")
                        .param("title", "")
                        .param("authorId", "1")
                        .param("genreIds", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/new"))
                .andExpect(model().attributeExists("bookRequest"))
                .andExpect(model().attributeHasFieldErrors("bookRequest", "title"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres));
    }

    @Test
    @DisplayName("при запросе GET /books/{id} - должен вернуть страницу редактирования книги")
    void testEditBook() throws Exception {
        when(bookService.findBookById(1L)).thenReturn(Optional.of(book));

        try (MockedStatic<BookMapper> mockedStatic = Mockito.mockStatic(BookMapper.class)) {
            mockedStatic.when(() -> BookMapper.toBookRequestDTO(book)).thenReturn(bookRequest);

            mvc.perform(get("/books/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("books/edit"))
                    .andExpect(model().attribute("authors", authors))
                    .andExpect(model().attribute("genres", genres))
                    .andExpect(model().attribute("bookRequest", bookRequest));
        }
    }

    @Test
    @DisplayName("при запросе GET /books/{id} с несуществующим ID - должен вернуть страницу 404")
    void testEditBook_withInvalidId() throws Exception {
        when(bookService.findById(123L)).thenThrow(new EntityNotFoundException(BOOK_NOT_FOUND));

        mvc.perform(get("/books/123"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("errorMessage", BOOK_NOT_FOUND));
    }

    @Test
    @DisplayName("при запросе PATCH /books - должен обновить книгу и перенаправить на список книг")
    void testUpdateBook() throws Exception {
        mvc.perform(patch("/books")
                        .param("id", bookRequest.getId().toString())
                        .param("title", bookRequest.getTitle())
                        .param("authorId", bookRequest.getAuthorId().toString())
                        .param("genreIds", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).update(
                bookRequest.getId(),
                bookRequest.getTitle(),
                bookRequest.getAuthorId(),
                bookRequest.getGenreIds());
    }

    @Test
    @DisplayName("При запросе PATCH /books с некорректными данными - должен вернуться к странице редактирования книги")
    void testUpdateBook_withValidationErrors() throws Exception {
        mvc.perform(patch("/books")
                        .param("id", bookRequest.getId().toString())
                        .param("title", "")
                        .param("authorId", bookRequest.getAuthorId().toString())
                        .param("genreIds", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/edit"))
                .andExpect(model().attributeExists("bookRequest"))
                .andExpect(model().attributeHasFieldErrors("bookRequest", "title"))
                .andExpect(model().attribute("authors", authors))
                .andExpect(model().attribute("genres", genres));
    }

    @Test
    @DisplayName("при запросе DELETE /books/{id} - должен удалить книгу и перенаправить на список книг")
    void testDeleteBook() throws Exception {
        mvc.perform(delete("/books/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).deleteById(1L);
    }
}
