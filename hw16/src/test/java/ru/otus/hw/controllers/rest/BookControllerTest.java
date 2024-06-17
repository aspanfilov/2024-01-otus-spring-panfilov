package ru.otus.hw.controllers.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.dtos.BookCommentDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.BookRequestDTO;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.BookCommentService;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Класс BookController")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    private static final String BOOK_NOT_FOUND = "Book with id 123 not found";
    private static final Long NON_EXISTENT_ID = 123L;
    private static final Long BOOK_ID = 1L;


    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper mapper;

    private Book book;
    private BookDTO bookDTO;
    private BookRequestDTO bookRequestDTO;

    @BeforeEach
    void setUp() {

        Author author = new Author(1L, "author");
        Genre genre = new Genre(1L, "genre");
        book = new Book(BOOK_ID, "book", author, List.of(genre));

        AuthorDTO authorDTO = new AuthorDTO(1L, "author");
        GenreDTO genreDTO = new GenreDTO(1L, "genre");
        bookDTO = BookDTO.builder()
                .id(BOOK_ID)
                .title("book")
                .author(authorDTO)
                .genres(List.of(genreDTO))
                .build();

        bookRequestDTO = new BookRequestDTO(BOOK_ID, "title", 1L, Set.of(1L));
    }

    @Test
    @DisplayName("при запросе GET /api/v1/books - должен вернуться список dto авторов")
    void testGetBooks() throws Exception {
        List<BookDTO> bookDTOs = List.of(bookDTO);

        when(bookService.findAll()).thenReturn(bookDTOs);

        try (MockedStatic<BookMapper> mockedStatic = Mockito.mockStatic(BookMapper.class)) {
            mockedStatic.when(() -> BookMapper.toBookDTO(book)).thenReturn(bookDTO);

            mvc.perform(get("/api/v1/books"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(mapper.writeValueAsString(bookDTOs)));
        }
    }

    @Test
    @DisplayName("при запросе GET /api/v1/books/{id} - должен вернуть dto книги")
    void testGetBook() throws Exception {
        when(bookService.findById(BOOK_ID)).thenReturn(Optional.of(bookDTO));

        try (MockedStatic<BookMapper> mockedStatic = Mockito.mockStatic(BookMapper.class)) {

            mockedStatic.when(() -> BookMapper.toBookDTO(book)).thenReturn(bookDTO);

            mvc.perform(get("/api/v1/books/{id}", BOOK_ID))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(mapper.writeValueAsString(bookDTO)));
        }
    }

    @Test
    @DisplayName("при запросе GET /api/v1/books/{id} с несуществующим ID - должен бросить исключение")
    void testGetBook_withNonExistentId() throws Exception {
        when(bookService.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/books/{id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(BOOK_NOT_FOUND));
    }

    @Test
    @DisplayName("при запросе POST /api/v1/books - должен создать нового автора")
    void testCreateBook() throws Exception {

        try (MockedStatic<BookMapper> mockedStatic = Mockito.mockStatic(BookMapper.class)) {

            when(bookService.insert(
                    bookRequestDTO.getTitle(),
                    bookRequestDTO.getAuthorId(),
                    bookRequestDTO.getGenreIds()))
                    .thenReturn(bookDTO);
            mockedStatic.when(() -> BookMapper.toBookDTO(book)).thenReturn(bookDTO);

            mvc.perform(post("/api/v1/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.title").value(book.getTitle()))
                    .andExpect(jsonPath("$.author.id").value(book.getAuthor().getId()))
                    .andExpect(jsonPath("$.genres[*].id", containsInAnyOrder(1)));

            verify(bookService).insert(
                    bookRequestDTO.getTitle(),
                    bookRequestDTO.getAuthorId(),
                    bookRequestDTO.getGenreIds());
        }
    }

    @Test
    @DisplayName("при запросе POST /api/v1/books с некорректными данными - должен вернуть статус ошибки")
    void testCreateBook_withValidationErrors() throws Exception {

        bookDTO.setTitle("");

        mvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("при запросе PUT /api/v1/books/{id} - должен обновить автора")
    void testUpdateBook() throws Exception {
        try (MockedStatic<BookMapper> mockedStatic = Mockito.mockStatic(BookMapper.class)) {

            when(bookService.update(
                    bookRequestDTO.getId(),
                    bookRequestDTO.getTitle(),
                    bookRequestDTO.getAuthorId(),
                    bookRequestDTO.getGenreIds()))
                    .thenReturn(bookDTO);
            mockedStatic.when(() -> BookMapper.toBookDTO(book)).thenReturn(bookDTO);

            mvc.perform(put("/api/v1/books/{id}", BOOK_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(book.getId()))
                    .andExpect(jsonPath("$.title").value(book.getTitle()))
                    .andExpect(jsonPath("$.author.id").value(book.getAuthor().getId()))
                    .andExpect(jsonPath("$.genres[*].id", containsInAnyOrder(1)));


            verify(bookService).update(
                    bookRequestDTO.getId(),
                    bookRequestDTO.getTitle(),
                    bookRequestDTO.getAuthorId(),
                    bookRequestDTO.getGenreIds());
        }
    }

    @Test
    @DisplayName("при запросе PUT /api/v1/books/{id} с некорректными данными - должен вернуть статус ошибки")
    void testUpdateBook_withValidationErrors() throws Exception {

        bookDTO.setTitle("");

        mvc.perform(put("/api/v1/books/{id}", bookDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("при запросе DELETE /books/{id} - должен удалить автора")
    void testDeleteBook() throws Exception {

        mvc.perform(delete("/api/v1/books/{id}", bookDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookService).deleteById(bookDTO.getId());
    }

}
