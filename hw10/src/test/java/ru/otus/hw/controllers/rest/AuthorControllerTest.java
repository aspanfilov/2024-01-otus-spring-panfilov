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
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Класс AuthorController")
@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    private static final String AUTHOR_NOT_FOUND = "Author with id 123 not found";
    private static final Long NON_EXISTENT_ID = 123L;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper mapper;

    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "author1");
        authorDTO = new AuthorDTO(1L, "author1");
    }

    @Test
    @DisplayName("при запросе GET /api/v1/authors - должен вернуться список dto авторов")
    void testGetAuthors() throws Exception {
        List<AuthorDTO> authorDTOs = List.of(authorDTO);
        List<Author> authors = List.of(author);

        when(authorService.findAll()).thenReturn(authors);

        try (MockedStatic<AuthorMapper> mockedStatic = Mockito.mockStatic(AuthorMapper.class)) {
            mockedStatic.when(() -> AuthorMapper.toAuthorDto(author)).thenReturn(authorDTO);

            mvc.perform(get("/api/v1/authors"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(mapper.writeValueAsString(authorDTOs)));
        }
    }

    @Test
    @DisplayName("при запросе GET /api/v1/authors/{id} - должен вернуть dto автора")
    void testGetAuthor() throws Exception {
        when(authorService.findById(1L)).thenReturn(Optional.of(author));

        try (MockedStatic<AuthorMapper> mockedStatic = Mockito.mockStatic(AuthorMapper.class)) {

            mockedStatic.when(() -> AuthorMapper.toAuthorDto(author)).thenReturn(authorDTO);

            mvc.perform(get("/api/v1/authors/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(mapper.writeValueAsString(authorDTO)));
        }
    }

    @Test
    @DisplayName("при запросе GET /api/v1/authors/{id} с несуществующим ID - должен бросить исключение")
    void testGetAuthor_withNonExistentId() throws Exception {
        when(authorService.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/authors/{id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(AUTHOR_NOT_FOUND));
    }

    @Test
    @DisplayName("при запросе POST /api/v1/authors - должен создать нового автора")
    void testCreateAuthor() throws Exception {

        try (MockedStatic<AuthorMapper> mockedStatic = Mockito.mockStatic(AuthorMapper.class)) {

            when(authorService.insert(authorDTO.getFullName())).thenReturn(author);
            mockedStatic.when(() -> AuthorMapper.toAuthorDto(author)).thenReturn(authorDTO);

            mvc.perform(post("/api/v1/authors")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(authorDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.fullName").value(author.getFullName()));

            verify(authorService).insert(authorDTO.getFullName());
        }
    }

    @Test
    @DisplayName("при запросе POST /api/v1/authors с некорректными данными - должен вернуть статус ошибки")
    void testCreateAuthor_withValidationErrors() throws Exception {

        authorDTO.setFullName("");

        mvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authorDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("при запросе PUT /api/v1/authors/{id} - должен обновить автора")
    void testUpdateAuthor() throws Exception {
        try (MockedStatic<AuthorMapper> mockedStatic = Mockito.mockStatic(AuthorMapper.class)) {

            when(authorService.update(authorDTO.getId(), authorDTO.getFullName()))
                    .thenReturn(author);
            mockedStatic.when(() -> AuthorMapper.toAuthorDto(author))
                    .thenReturn(authorDTO);

            mvc.perform(put("/api/v1/authors/{id}", authorDTO.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(authorDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(author.getId()))
                    .andExpect(jsonPath("$.fullName").value(author.getFullName()));

            verify(authorService).update(authorDTO.getId(), authorDTO.getFullName());
        }
    }

    @Test
    @DisplayName("при запросе PUT /api/v1/authors/{id} с некорректными данными - должен вернуть статус ошибки")
    void testUpdateAuthor_withValidationErrors() throws Exception {

        authorDTO.setFullName("");

        mvc.perform(put("/api/v1/authors/{id}", authorDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authorDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("при запросе DELETE /authors/{id} - должен удалить автора")
    void testDeleteAuthor() throws Exception {

        mvc.perform(delete("/api/v1/authors/{id}", authorDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(authorService).deleteById(authorDTO.getId());
    }

}
