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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.dtos.BookCommentDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookCommentMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.User;
import ru.otus.hw.security.CustomUserDetails;
import ru.otus.hw.services.BookCommentService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Класс BookCommentController")
@WebMvcTest(BookCommentController.class)
public class BookCommentControllerTest {

    private static final String BOOK_COMMENT_NOT_FOUND = "Book comment with id 123 not found";
    private static final Long NON_EXISTENT_ID = 123L;
    private static final Long BOOK_ID = 1L;
    private static final Long BOOK_COMMENT_ID = 1L;


    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookCommentService bookCommentService;

    @Autowired
    private ObjectMapper mapper;

    private BookComment bookComment;
    private BookCommentDTO bookCommentDTO;

    @BeforeEach
    void setUp() {
        Author author = new Author(1L, "author");
        Genre genre = new Genre(1L, "genre");
        Book book = new Book(BOOK_ID, "book", author, List.of(genre));
        bookComment = new BookComment(BOOK_COMMENT_ID, "comment", book);

        AuthorDTO authorDTO = new AuthorDTO(1L, "author");
        GenreDTO genreDTO = new GenreDTO(1L, "genre");
        BookDTO bookDTO = BookDTO.builder()
                .id(BOOK_ID)
                .title("book")
                .author(authorDTO)
                .genres(List.of(genreDTO))
                .build();
        bookCommentDTO = BookCommentDTO.builder()
                .id(BOOK_COMMENT_ID)
                .commentText("comment")
                .book(bookDTO)
                .build();
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /api/v1/books/{bookId}/comments - должен вернуться список dto")
    void testGetBookComments() throws Exception {
        List<BookCommentDTO> bookCommentDTOs = List.of(bookCommentDTO);

        when(bookCommentService.findAllByBookId(BOOK_ID)).thenReturn(bookCommentDTOs);

        try (MockedStatic<BookCommentMapper> mockedStatic = Mockito.mockStatic(BookCommentMapper.class)) {

            mockedStatic.when(() -> BookCommentMapper.toBookCommentDTO(bookComment)).thenReturn(bookCommentDTO);

            mvc.perform(get("/api/v1/books/{bookId}/comments", BOOK_ID))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(mapper.writeValueAsString(bookCommentDTOs)));
        }
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /api/v1/books/{bookid}/comments/{bookCommentId} - должен вернуть dto")
    void testGetBookComment() throws Exception {
        when(bookCommentService.findById(BOOK_COMMENT_ID)).thenReturn(Optional.of(bookCommentDTO));

        try (MockedStatic<BookCommentMapper> mockedStatic = Mockito.mockStatic(BookCommentMapper.class)) {

            mockedStatic.when(() -> BookCommentMapper.toBookCommentDTO(bookComment)).thenReturn(bookCommentDTO);

            mvc.perform(get("/api/v1/books/{bookid}/comments/{bookCommentId}", BOOK_ID, BOOK_COMMENT_ID))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(mapper.writeValueAsString(bookCommentDTO)));
        }
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /api/v1/books/{bookid}/comments/{bookCommentId} с несуществующим ID - должен бросить исключение")
    void testGetBookComment_withNonExistentId() throws Exception {
        when(bookCommentService.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/books/{bookid}/comments/{bookCommentId}", BOOK_ID, NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(BOOK_COMMENT_NOT_FOUND));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе POST /api/v1/books/{bookId}/comments - должен создать новый")
    void testCreateBookComment() throws Exception {

        try (MockedStatic<BookCommentMapper> mockedStatic = Mockito.mockStatic(BookCommentMapper.class)) {

            when(bookCommentService.insert(BOOK_ID, bookCommentDTO.getCommentText()))
                    .thenReturn(bookCommentDTO);
            mockedStatic.when(() -> BookCommentMapper.toBookCommentDTO(bookComment))
                    .thenReturn(bookCommentDTO);

            mvc.perform(post("/api/v1/books/{bookId}/comments", BOOK_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookCommentDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.commentText").value(bookComment.getCommentText()))
                    .andExpect(jsonPath("$.book").value(bookComment.getBook()));

            verify(bookCommentService).insert(BOOK_ID, bookCommentDTO.getCommentText());
        }
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе POST /api/v1/books/{bookId}/comments с некорректными данными - должен вернуть статус ошибки")
    void testCreateBookComment_withValidationErrors() throws Exception {

        bookCommentDTO.setCommentText("");

        mvc.perform(post("/api/v1/books/{bookId}/comments", BOOK_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookCommentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе PUT /api/v1/books/{bookId}/comments/{bookCommentId} - должен обновить")
    void testUpdateBookComment() throws Exception {
        try (MockedStatic<BookCommentMapper> mockedStatic = Mockito.mockStatic(BookCommentMapper.class)) {

            when(bookCommentService.update(BOOK_COMMENT_ID, BOOK_ID, bookCommentDTO.getCommentText()))
                    .thenReturn(bookCommentDTO);
            mockedStatic.when(() -> BookCommentMapper.toBookCommentDTO(bookComment))
                    .thenReturn(bookCommentDTO);

            mvc.perform(put("/api/v1/books/{bookId}/comments/{bookCommentId}", BOOK_ID, BOOK_COMMENT_ID)
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(bookCommentDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(bookComment.getId()))
                    .andExpect(jsonPath("$.commentText").value(bookComment.getCommentText()))
                    .andExpect(jsonPath("$.book").value(bookComment.getBook()));

            verify(bookCommentService).update(BOOK_COMMENT_ID, BOOK_ID, bookCommentDTO.getCommentText());
        }
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе PUT /api/v1/books/{bookId}/comments/{bookCommentId} с некорректными данными - должен вернуть статус ошибки")
    void testUpdateBookComment_withValidationErrors() throws Exception {

        bookCommentDTO.setCommentText("");

        mvc.perform(put("/api/v1/books/{bookId}/comments/{bookCommentId}", BOOK_ID, BOOK_COMMENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookCommentDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе DELETE /api/v1/books/{bookId}/comments/{bookCommentId} - должен удалить автора")
    void testDeleteAuthor() throws Exception {

        mvc.perform(delete("/api/v1/books/{bookId}/comments/{bookCommentId}", BOOK_ID, BOOK_COMMENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookCommentService).deleteById(bookCommentDTO.getId());
    }

    @Test
    @DisplayName("при запросе GET /api/v1/books/{bookId}/comments без аутентификации - должен вернуть 401")
    void testGetBookCommentsUnauthorized() throws Exception {
        mvc.perform(get("/api/v1/books/{bookId}/comments", BOOK_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе GET /api/v1/books/{bookid}/comments/{bookCommentId} без аутентификации - должен вернуть 401")
    void testGetBookCommentUnauthorized() throws Exception {
        mvc.perform(get("/api/v1/books/{bookid}/comments/{bookCommentId}", BOOK_ID, BOOK_COMMENT_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе POST /api/v1/books/{bookId}/comments без аутентификации - должен вернуть 401")
    void testCreateBookCommentUnauthorized() throws Exception {
        mvc.perform(post("/api/v1/books/{bookId}/comments", BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookCommentDTO))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе PUT /api/v1/books/{bookId}/comments/{bookCommentId} без аутентификации - должен вернуть 401")
    void testUpdateBookCommentUnauthorized() throws Exception {
        mvc.perform(put("/api/v1/books/{bookId}/comments/{bookCommentId}", BOOK_ID, BOOK_COMMENT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookCommentDTO))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе DELETE /api/v1/books/{bookId}/comments/{bookCommentId} без аутентификации - должен вернуть 401")
    void testDeleteBookCommentUnauthorized() throws Exception {
        mvc.perform(delete("/api/v1/books/{bookId}/comments/{bookCommentId}", BOOK_ID, BOOK_COMMENT_ID)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}
