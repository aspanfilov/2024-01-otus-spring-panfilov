package ru.otus.hw.controllers.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.otus.hw.config.AppConfig;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.controllers.mvc.AuthorPageController;
import ru.otus.hw.controllers.mvc.BookCommentPageController;
import ru.otus.hw.controllers.mvc.BookPageController;
import ru.otus.hw.controllers.mvc.GenrePageController;
import ru.otus.hw.controllers.rest.AuthorController;
import ru.otus.hw.controllers.rest.BookCommentController;
import ru.otus.hw.controllers.rest.BookController;
import ru.otus.hw.controllers.rest.GenreController;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.dtos.BookCommentDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.BookRequestDTO;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.dtos.UserViewDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookCommentService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка доступности контроллеров по аутентификации")
@WebMvcTest({
        AuthorPageController.class,
        GenrePageController.class,
        BookPageController.class,
        BookCommentPageController.class,
        AuthorController.class,
        GenreController.class,
        BookController.class,
        BookCommentController.class})
@Import({SecurityConfig.class, AppConfig.class})
public class AuthenticationTest {

    private static final String EXPECTED_REDIRECT_URL = "http://localhost/login";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private BookService bookService;

    @MockBean
    private BookCommentService bookCommentService;

    @Autowired
    private ObjectMapper mapper;

    private static AuthorDTO authorDTO;
    private static GenreDTO genreDTO;
    private static BookDTO bookDTO;
    private static BookRequestDTO bookRequestDTO;
    private static UserViewDTO userViewDTO;
    private static BookCommentDTO bookCommentDTO;

    @BeforeEach
    void setUp() {
        var author = new Author(1L, "author1");
        authorDTO = new AuthorDTO(1L, "author1");

        var genre = new Genre(1L, "genre1");
        genreDTO = new GenreDTO(1L, "genre1");

        bookDTO = BookDTO.builder()
                .id(1L)
                .title("book")
                .author(authorDTO)
                .genres(List.of(genreDTO))
                .build();
        bookRequestDTO = new BookRequestDTO(1L, "title", 1L, Set.of(1L));

        userViewDTO = UserViewDTO.builder()
                .id(1L)
                .username("admin")
                .build();
        bookCommentDTO = getBookCommentDTO();

        when(authorService.findById(1L)).thenReturn(Optional.of(author));
        when(authorService.insert(authorDTO.getFullName())).thenReturn(author);
        when(authorService.update(authorDTO.getId(), authorDTO.getFullName())).thenReturn(author);

        when(genreService.findById(1L)).thenReturn(Optional.of(genre));
        when(genreService.insert(genreDTO.getName())).thenReturn(genre);
        when(genreService.update(genreDTO.getId(), genreDTO.getName())).thenReturn(genre);

        when(bookService.findById(1L)).thenReturn(Optional.of(bookDTO));
        when(bookService.insert(
                bookRequestDTO.getTitle(),
                bookRequestDTO.getAuthorId(),
                bookRequestDTO.getGenreIds()))
                .thenReturn(bookDTO);
        when(bookService.update(
                bookRequestDTO.getId(),
                bookRequestDTO.getTitle(),
                bookRequestDTO.getAuthorId(),
                bookRequestDTO.getGenreIds()))
                .thenReturn(bookDTO);

        when(bookCommentService.findById(1L)).thenReturn(Optional.of(bookCommentDTO));
        when(bookCommentService.insert(bookCommentDTO)).thenReturn(bookCommentDTO);
        when(bookCommentService.update(1L, bookCommentDTO)).thenReturn(bookCommentDTO);
    }

    static BookCommentDTO getBookCommentDTO() {
        return BookCommentDTO.builder()
                .id(1L)
                .commentText("comment")
                .book(bookDTO)
                .user(userViewDTO)
                .build();
    }

    static Stream<Arguments> endpointsProvider() {
        return Stream.of(
                Arguments.of("GET", "/authors", true, "AUTHOR_READ", 200, null),
                Arguments.of("GET", "/authors/new", true, "AUTHOR_WRITE", 200, null),
                Arguments.of("GET", "/authors/1", true, "AUTHOR_READ", 200, null),
                Arguments.of("GET", "/authors", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/authors/new", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/authors/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/authors", false, "", 302, null),
                Arguments.of("GET", "/authors/new", false, "", 302, null),
                Arguments.of("GET", "/authors/1", false, "", 302, null),

                Arguments.of("GET", "/genres", true, "GENRE_READ", 200, null),
                Arguments.of("GET", "/genres/new", true, "GENRE_WRITE", 200, null),
                Arguments.of("GET", "/genres/1", true, "GENRE_READ", 200, null),
                Arguments.of("GET", "/genres", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/genres/new", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/genres/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/genres", false, "", 302, null),
                Arguments.of("GET", "/genres/new", false, "", 302, null),
                Arguments.of("GET", "/genres/1", false, "", 302, null),

                Arguments.of("GET", "/books", true, "BOOK_READ", 200, null),
                Arguments.of("GET", "/books/new", true, "BOOK_WRITE", 200, null),
                Arguments.of("GET", "/books/1", true, "BOOK_READ", 200, null),
                Arguments.of("GET", "/books", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/books/new", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/books/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/books", false, "", 302, null),
                Arguments.of("GET", "/books/new", false, "", 302, null),
                Arguments.of("GET", "/books/1", false, "", 302, null),

                Arguments.of("GET", "/books/1/comments", true, "COMMENT_READ", 200, null),
                Arguments.of("GET", "/books/1/comments/new", true, "COMMENT_WRITE", 200, null),
                Arguments.of("GET", "/books/1/comments/1", true, "COMMENT_READ", 200, null),
                Arguments.of("GET", "/books/1/comments", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/books/1/comments/new", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/books/1/comments/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/books/1/comments", false, "", 302, null),
                Arguments.of("GET", "/books/1/comments/new", false, "", 302, null),
                Arguments.of("GET", "/books/1/comments/1", false, "", 302, null),


                Arguments.of("GET", "/api/v1/authors", true, "AUTHOR_READ", 200, null),
                Arguments.of("GET", "/api/v1/authors/1", true, "AUTHOR_READ", 200, null),
                Arguments.of("POST", "/api/v1/authors", true, "AUTHOR_WRITE", 200,
                        new AuthorDTO(1L, "author1")),
                Arguments.of("PUT", "/api/v1/authors/1", true, "AUTHOR_WRITE", 200,
                        new AuthorDTO(1L, "author1")),
                Arguments.of("DELETE", "/api/v1/authors/1", true, "AUTHOR_DELETE", 200, null),
                Arguments.of("GET", "/api/v1/authors", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/api/v1/authors/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("POST", "/api/v1/authors", true, "WRONG_AUTHORITY", 403,
                        new AuthorDTO(1L, "author1")),
                Arguments.of("PUT", "/api/v1/authors/1", true, "WRONG_AUTHORITY", 403,
                        new AuthorDTO(1L, "author1")),
                Arguments.of("DELETE", "/api/v1/authors/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/api/v1/authors", false, "", 302, null),
                Arguments.of("GET", "/api/v1/authors/1", false, "", 302, null),
                Arguments.of("POST", "/api/v1/authors", false, "", 302,
                        new AuthorDTO(1L, "author1")),
                Arguments.of("PUT", "/api/v1/authors/1", false, "", 302, null),
                Arguments.of("DELETE", "/api/v1/authors/1", false, "", 302, null),

                Arguments.of("GET", "/api/v1/genres", true, "GENRE_READ", 200, null),
                Arguments.of("GET", "/api/v1/genres/1", true, "GENRE_READ", 200, null),
                Arguments.of("POST", "/api/v1/genres", true, "GENRE_WRITE", 200,
                        new GenreDTO(1L, "genre1")),
                Arguments.of("PUT", "/api/v1/genres/1", true, "GENRE_WRITE", 200,
                        new GenreDTO(1L, "genre1")),
                Arguments.of("DELETE", "/api/v1/genres/1", true, "GENRE_DELETE", 200, null),
                Arguments.of("GET", "/api/v1/genres", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/api/v1/genres/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("POST", "/api/v1/genres", true, "WRONG_AUTHORITY", 403,
                        new GenreDTO(1L, "genre1")),
                Arguments.of("PUT", "/api/v1/genres/1", true, "WRONG_AUTHORITY", 403,
                        new GenreDTO(1L, "genre1")),
                Arguments.of("DELETE", "/api/v1/genres/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/api/v1/genres", false, "", 302, null),
                Arguments.of("GET", "/api/v1/genres/1", false, "", 302, null),
                Arguments.of("POST", "/api/v1/genres", false, "", 302,
                        new GenreDTO(1L, "genre1")),
                Arguments.of("PUT", "/api/v1/genres/1", false, "", 302,
                        new GenreDTO(1L, "genre1")),
                Arguments.of("DELETE", "/api/v1/genres/1", false, "", 302, null),

                Arguments.of("GET", "/api/v1/books", true, "BOOK_READ", 200, null),
                Arguments.of("GET", "/api/v1/books/1", true, "BOOK_READ", 200, null),
                Arguments.of("POST", "/api/v1/books", true, "BOOK_WRITE", 200,
                        new BookRequestDTO(1L, "title", 1L, Set.of(1L))),
                Arguments.of("PUT", "/api/v1/books/1", true, "BOOK_WRITE", 200,
                        new BookRequestDTO(1L, "title", 1L, Set.of(1L))),
                Arguments.of("DELETE", "/api/v1/books/1", true, "BOOK_DELETE", 200, null),
                Arguments.of("GET", "/api/v1/books", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/api/v1/books/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("POST", "/api/v1/books", true, "WRONG_AUTHORITY", 403,
                        new BookRequestDTO(1L, "title", 1L, Set.of(1L))),
                Arguments.of("PUT", "/api/v1/books/1", true, "WRONG_AUTHORITY", 403,
                        new BookRequestDTO(1L, "title", 1L, Set.of(1L))),
                Arguments.of("DELETE", "/api/v1/books/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/api/v1/books", false, "", 302, null),
                Arguments.of("GET", "/api/v1/books/1", false, "", 302, null),
                Arguments.of("POST", "/api/v1/books", false, "", 302,
                        new BookRequestDTO(1L, "title", 1L, Set.of(1L))),
                Arguments.of("PUT", "/api/v1/books/1", false, "", 302,
                        new BookRequestDTO(1L, "title", 1L, Set.of(1L))),
                Arguments.of("DELETE", "/api/v1/books/1", false, "", 302, null),

                Arguments.of("GET", "/api/v1/books/1/comments", true, "COMMENT_READ", 200, null),
                Arguments.of("GET", "/api/v1/books/1/comments/1", true, "COMMENT_READ", 200, null),
                Arguments.of("POST", "/api/v1/books/1/comments", true, "COMMENT_WRITE", 200,
                        getBookCommentDTO()),
                Arguments.of("PUT", "/api/v1/books/1/comments/1", true, "COMMENT_WRITE", 200,
                        getBookCommentDTO()),
                Arguments.of("DELETE", "/api/v1/books/1/comments/1", true, "COMMENT_DELETE", 200, null),
                Arguments.of("GET", "/api/v1/books/1/comments", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/api/v1/books/1/comments/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("POST", "/api/v1/books/1/comments", true, "WRONG_AUTHORITY", 403,
                        getBookCommentDTO()),
                Arguments.of("PUT", "/api/v1/books/1/comments/1", true, "WRONG_AUTHORITY", 403,
                        getBookCommentDTO()),
                Arguments.of("DELETE", "/api/v1/books/1/comments/1", true, "WRONG_AUTHORITY", 403, null),
                Arguments.of("GET", "/api/v1/books/1/comments", false, "", 302, null),
                Arguments.of("GET", "/api/v1/books/1/comments/1", false, "", 302, null),
                Arguments.of("POST", "/api/v1/books/1/comments", false, "", 302,
                        getBookCommentDTO()),
                Arguments.of("PUT", "/api/v1/books/1/comments/1", false, "", 302,
                        getBookCommentDTO()),
                Arguments.of("DELETE", "/api/v1/books/1/comments/1", false, "", 302, null)
        );
    }

    @ParameterizedTest
    @MethodSource("endpointsProvider")
    @DisplayName("Тест доступности очередного эндпоинта")
    void testEndpoint(String method, String url, boolean authenticated, String authority, int expectedStatus, Object dto) throws Exception {

        var requestBuilder = request(method, url, dto);
        if (authenticated) {
            requestBuilder.with(
                    user("user").authorities(new SimpleGrantedAuthority(authority)));
        }

        ResultActions resultActions = mvc.perform(requestBuilder);

        resultActions.andExpect(status().is(expectedStatus));
        if (!authenticated) {
            resultActions.andExpect(redirectedUrl(EXPECTED_REDIRECT_URL));
        }
    }

    private MockHttpServletRequestBuilder request(String method, String url, Object dto) throws JsonProcessingException {
        var payload = dto != null ? mapper.writeValueAsString(dto) : "";

        return switch (method) {
            case "GET" -> get(url);
            case "POST" -> post(url)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload);
            case "PUT" -> put(url)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload);
            case "DELETE" -> delete(url);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };
    }
}
