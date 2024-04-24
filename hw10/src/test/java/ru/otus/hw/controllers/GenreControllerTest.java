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
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

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

@DisplayName("Класс GenreController")
@WebMvcTest(GenreController.class)
public class GenreControllerTest {
    private static final String GENRE_NOT_FOUND = "Genre with id 123 not found";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    private Genre genre;
    private GenreDTO genreDTO;

    @BeforeEach
    void setUp() {
        genre = new Genre(1L, "genre1");
        genreDTO = new GenreDTO(1L, "genre1");
    }

    @DisplayName("при запросе GET /genres - должен вернуть список жанров")
    @Test
    void testListGenres() throws Exception {
        List<Genre> genres = List.of(genre);
        List<GenreDTO> genreDTOs = List.of(genreDTO);

        when(genreService.findAll()).thenReturn(genres);

        try (MockedStatic<GenreMapper> mockedStatic = Mockito.mockStatic(GenreMapper.class)) {
            mockedStatic.when(() -> GenreMapper.toGenreDto(genre)).thenReturn(genreDTO);

            mvc.perform(get("/genres"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("genres/list"))
                    .andExpect(model().attribute("genres", genreDTOs));
        }
    }

    @DisplayName("при запросе GET /genres/new - возвращается страница создания нового жанра")
    @Test
    void testNewGenre() throws Exception {
        mvc.perform(get("/genres/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("genres/new"))
                .andExpect(model().attributeExists("genre"));
    }

    @DisplayName("при запросе POST /genres - должен создать новый жанр и перенаправить на список жанров")
    @Test
    void testCreateGenre() throws Exception {
        mvc.perform(post("/genres")
                        .param("name", "genre1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/genres"));

        verify(genreService).insert("genre1");

    }

    @DisplayName("При запросе POST /genres с некорректными данными - должен вернуться к странице создания жанра")
    @Test
    void testCreateGenre_withValidationErrors() throws Exception {
        mvc.perform(post("/genres")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("genres/new"))
                .andExpect(model().attributeExists("genre"))
                .andExpect(model().attributeHasFieldErrors("genre", "name"));
    }

    @Test
    @DisplayName("при запросе GET /genres/{id} - должен вернуть страницу редактирования жанра")
    void testEditGenre() throws Exception {
        when(genreService.findById(1L)).thenReturn(Optional.of(genre));

        try (MockedStatic<GenreMapper> mockedStatic = Mockito.mockStatic(GenreMapper.class)) {
            mockedStatic.when(() -> GenreMapper.toGenreDto(genre)).thenReturn(genreDTO);

            mvc.perform(get("/genres/1"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("genres/edit"))
                    .andExpect(model().attribute("genre", genreDTO));
        }
    }

    @Test
    @DisplayName("при запросе GET /genres/{id} с несуществующим ID - должен вернуть страницу 404")
    void testEditGenre_withInvalidId() throws Exception {
        when(genreService.findById(123L)).thenThrow(new EntityNotFoundException(GENRE_NOT_FOUND));

        mvc.perform(get("/genres/123"))
                .andExpect(status().is4xxClientError())
                .andExpect(view().name("error/404"))
                .andExpect(model().attribute("errorMessage", GENRE_NOT_FOUND));
    }

    @Test
    @DisplayName("при запросе PATCH /genres - должен обновить жанр и перенаправить на список жанров")
    void testUpdateGenre() throws Exception {
        when(genreService.update(1L, "genre1")).thenReturn(genre);

        mvc.perform(patch("/genres")
                        .param("id", "1")
                        .param("name", "genre1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/genres"));

        verify(genreService).update(1, "genre1");
    }

    @Test
    @DisplayName("При запросе PATCH /genres с некорректными данными - должен вернуться к странице редактирования жанра")
    void testUpdateGenre_withValidationErrors() throws Exception {
        mvc.perform(patch("/genres")
                        .param("name", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("genres/edit"))
                .andExpect(model().attributeExists("genre"))
                .andExpect(model().attributeHasFieldErrors("genre", "name"));
    }

    @Test
    @DisplayName("при запросе DELETE /genres/{id} - должен удалить жанр и перенаправить на список жанров")
    void testDeleteGenre() throws Exception {
        mvc.perform(delete("/genres/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/genres"));

        verify(genreService).deleteById(1L);
    }
}
