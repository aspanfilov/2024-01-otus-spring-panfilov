package ru.otus.hw.controllers.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.controllers.mvc.AuthorPageController;
import ru.otus.hw.controllers.mvc.BookCommentPageController;
import ru.otus.hw.controllers.mvc.BookPageController;
import ru.otus.hw.controllers.mvc.GenrePageController;
import ru.otus.hw.controllers.rest.AuthorController;
import ru.otus.hw.controllers.rest.BookCommentController;
import ru.otus.hw.controllers.rest.BookController;
import ru.otus.hw.controllers.rest.GenreController;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookCommentService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.stream.Stream;

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
@Import(SecurityConfig.class)
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

    static Stream<Arguments> endpointsProvider() {
        return Stream.of(
                Arguments.of("GET", "/authors", true, 200),
                Arguments.of("GET", "/authors/new", true, 200),
                Arguments.of("GET", "/authors/1", true, 200),
                Arguments.of("GET", "/authors", false, 302),
                Arguments.of("GET", "/authors/new", false, 302),
                Arguments.of("GET", "/authors/1", false, 302),

                Arguments.of("GET", "/genres", true, 200),
                Arguments.of("GET", "/genres/new", true, 200),
                Arguments.of("GET", "/genres/1", true, 200),
                Arguments.of("GET", "/genres", false, 302),
                Arguments.of("GET", "/genres/new", false, 302),
                Arguments.of("GET", "/genres/1", false, 302),

                Arguments.of("GET", "/books", true, 200),
                Arguments.of("GET", "/books/new", true, 200),
                Arguments.of("GET", "/books/1", true, 200),
                Arguments.of("GET", "/books", false, 302),
                Arguments.of("GET", "/books/new", false, 302),
                Arguments.of("GET", "/books/1", false, 302),

                Arguments.of("GET", "/books/1/comments", true, 200),
                Arguments.of("GET", "/books/1/comments/new", true, 200),
                Arguments.of("GET", "/books/1/comments/1", true, 200),
                Arguments.of("GET", "/books/1/comments", false, 302),
                Arguments.of("GET", "/books/1/comments/new", false, 302),
                Arguments.of("GET", "/books/1/comments/1", false, 302),

                Arguments.of("GET", "/api/v1/authors", true, 200),
                Arguments.of("GET", "/api/v1/authors", false, 302),
                Arguments.of("GET", "/api/v1/authors/1", false, 302),
                Arguments.of("POST", "/api/v1/authors", false, 302),
                Arguments.of("PUT", "/api/v1/authors/1", false, 302),
                Arguments.of("DELETE", "/api/v1/authors/1", false, 302),

                Arguments.of("GET", "/api/v1/genres", true, 200),
                Arguments.of("GET", "/api/v1/genres", false, 302),
                Arguments.of("GET", "/api/v1/genres/1", false, 302),
                Arguments.of("POST", "/api/v1/genres", false, 302),
                Arguments.of("PUT", "/api/v1/genres/1", false, 302),
                Arguments.of("DELETE", "/api/v1/genres/1", false, 302),

                Arguments.of("GET", "/api/v1/books", true, 200),
                Arguments.of("GET", "/api/v1/books", false, 302),
                Arguments.of("GET", "/api/v1/books/1", false, 302),
                Arguments.of("POST", "/api/v1/books", false, 302),
                Arguments.of("PUT", "/api/v1/books/1", false, 302),
                Arguments.of("DELETE", "/api/v1/books/1", false, 302),

                Arguments.of("GET", "/api/v1/books/1/comments", true, 200),
                Arguments.of("GET", "/api/v1/books/1/comments", false, 302),
                Arguments.of("GET", "/api/v1/books/1/comments/1", false, 302),
                Arguments.of("POST", "/api/v1/books/1/comments", false, 302),
                Arguments.of("PUT", "/api/v1/books/1/comments/1", false, 302),
                Arguments.of("DELETE", "/api/v1/books/1/comments/1", false, 302)
        );
    }

    @ParameterizedTest
    @MethodSource("endpointsProvider")
    @DisplayName("Тест доступности очередного эндпоинта")
    void testEndpoint(String method, String url, boolean authenticated, int expectedStatus) throws Exception {

        var requestBuilder = request(method, url);
        if (authenticated) {
            requestBuilder.with(user("user"));
        }

        ResultActions resultActions = mvc.perform(requestBuilder);

        resultActions.andExpect(status().is(expectedStatus));
        if (!authenticated) {
            resultActions.andExpect(redirectedUrl(EXPECTED_REDIRECT_URL));
        }
    }

    private MockHttpServletRequestBuilder request(String method, String url) {
        return switch (method) {
            case "GET" -> get(url);
            case "POST" -> post(url);
            case "PUT" -> put(url);
            case "DELETE" -> delete(url);
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        };
    }
}
