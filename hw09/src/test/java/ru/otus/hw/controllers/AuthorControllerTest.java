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
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.Optional;

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


@DisplayName("Класс AuthorController")
@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    private static final String AUTHOR_NOT_FOUND = "Author with id 123 not found";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "author1");
        authorDTO = new AuthorDTO(1L, "author1");
    }

    @DisplayName("при запросе GET /authors - должен вернуть список авторов")
    @Test
    void testListAuthors() throws Exception {
        List<Author> authors = List.of(author);
        List<AuthorDTO> authorDTOs = List.of(authorDTO);

        when(authorService.findAll()).thenReturn(authors);

        try (MockedStatic<AuthorMapper> mockedStatic = Mockito.mockStatic(AuthorMapper.class)) {
            mockedStatic.when(() -> AuthorMapper.toAuthorDto(author)).thenReturn(authorDTO);

            mvc.perform(get("/authors"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("authors/list"))
                    .andExpect(model().attribute("authors", authorDTOs));
        }
    }

    @DisplayName("при запросе GET /authors/new - возвращается страница создания нового автора")
    @Test
    void testNewAuthor() throws Exception {
        mvc.perform(get("/authors/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("authors/new"))
                .andExpect(model().attributeExists("author"));
    }

    @DisplayName("при запросе POST /authors - должен создать нового автора и перенаправить на список авторов")
    @Test
    void testCreateAuthor() throws Exception {
        mvc.perform(post("/authors")
                        .param("fullName", "author1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors"));

        verify(authorService).insert("author1");

    }

    @DisplayName("При запросе POST /authors с некорректными данными - должен вернуться к странице создания автора")
    @Test
    void testCreateAuthor_withValidationErrors() throws Exception {
        mvc.perform(post("/authors")
                        .param("fullName", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("authors/new"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeHasFieldErrors("author", "fullName"));
    }

    @Test
    @DisplayName("при запросе GET /authors/{id} - должен вернуть страницу редактирования автора")
    void testEditAuthor() throws Exception {
        when(authorService.findById(1L)).thenReturn(Optional.of(author));

        try (MockedStatic<AuthorMapper> mockedStatic = Mockito.mockStatic(AuthorMapper.class)) {
            mockedStatic.when(() -> AuthorMapper.toAuthorDto(author)).thenReturn(authorDTO);

            mvc.perform(get("/authors/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("authors/edit"))
                    .andExpect(model().attribute("author", authorDTO));
        }
    }

    @Test
    @DisplayName("при запросе GET /authors/{id} с несуществующим ID - должен вернуть страницу 404")
    void testEditAuthor_withInvalidId() throws Exception {
        when(authorService.findById(123L)).thenThrow(new EntityNotFoundException(AUTHOR_NOT_FOUND));

        mvc.perform(get("/authors/123"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("errorMessage", AUTHOR_NOT_FOUND));
    }

    @Test
    @DisplayName("при запросе PATCH /authors - должен обновить автора и перенаправить на список авторов")
    void testUpdateAuthor() throws Exception {
        when(authorService.update(1L, "author1")).thenReturn(author);

        mvc.perform(patch("/authors")
                        .param("id", "1")
                        .param("fullName", "author1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors"));

        verify(authorService).update(1, "author1");
    }

    @Test
    @DisplayName("При запросе PATCH /authors с некорректными данными - должен вернуться к странице редактирования автора")
    void testUpdateAuthor_withValidationErrors() throws Exception {
        mvc.perform(patch("/authors")
                        .param("fullName", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("authors/edit"))
                .andExpect(model().attributeExists("author"))
                .andExpect(model().attributeHasFieldErrors("author", "fullName"));
    }

    @Test
    @DisplayName("при запросе DELETE /authors/{id} - должен удалить автора и перенаправить на список авторов")
    void testDeleteAuthor() throws Exception {
        mvc.perform(delete("/authors/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/authors"));

        verify(authorService).deleteById(1L);
    }


}
