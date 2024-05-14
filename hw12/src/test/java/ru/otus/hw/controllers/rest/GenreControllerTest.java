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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

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

@DisplayName("Класс GenreController")
@WebMvcTest(GenreController.class)
public class GenreControllerTest {

    private static final String GENRE_NOT_FOUND = "Genre with id 123 not found";
    private static final Long NON_EXISTENT_ID = 123L;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService genreService;

    @Autowired
    private ObjectMapper mapper;

    private Genre genre;
    private GenreDTO genreDTO;

    @BeforeEach
    void setUp() {
        genre = new Genre(1L, "genre1");
        genreDTO = new GenreDTO(1L, "genre1");
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /api/v1/genres - должен вернуться список dto жанров")
    void testGetGenres() throws Exception {
        List<GenreDTO> genreDTOs = List.of(genreDTO);
        List<Genre> genres = List.of(genre);

        when(genreService.findAll()).thenReturn(genres);

        try (MockedStatic<GenreMapper> mockedStatic = Mockito.mockStatic(GenreMapper.class)) {
            mockedStatic.when(() -> GenreMapper.toGenreDto(genre)).thenReturn(genreDTO);

            mvc.perform(get("/api/v1/genres"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(mapper.writeValueAsString(genreDTOs)));
        }
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /api/v1/genres/{id} - должен вернуть dto жанра")
    void testGetGenre() throws Exception {
        when(genreService.findById(1L)).thenReturn(Optional.of(genre));

        try (MockedStatic<GenreMapper> mockedStatic = Mockito.mockStatic(GenreMapper.class)) {

            mockedStatic.when(() -> GenreMapper.toGenreDto(genre)).thenReturn(genreDTO);

            mvc.perform(get("/api/v1/genres/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(mapper.writeValueAsString(genreDTO)));
        }
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе GET /api/v1/genres/{id} с несуществующим ID - должен бросить исключение")
    void testGetGenre_withNonExistentId() throws Exception {
        when(genreService.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        mvc.perform(get("/api/v1/genres/{id}", NON_EXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(GENRE_NOT_FOUND));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе POST /api/v1/genres - должен создать нового жанра")
    void testCreateGenre() throws Exception {

        try (MockedStatic<GenreMapper> mockedStatic = Mockito.mockStatic(GenreMapper.class)) {

            when(genreService.insert(genreDTO.getName())).thenReturn(genre);
            mockedStatic.when(() -> GenreMapper.toGenreDto(genre)).thenReturn(genreDTO);

            mvc.perform(post("/api/v1/genres")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(genreDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value(genre.getName()));

            verify(genreService).insert(genreDTO.getName());
        }
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе POST /api/v1/genres с некорректными данными - должен вернуть статус ошибки")
    void testCreateGenre_withValidationErrors() throws Exception {

        genreDTO.setName("");

        mvc.perform(post("/api/v1/genres")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(genreDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе PUT /api/v1/genres/{id} - должен обновить жанр")
    void testUpdateGenre() throws Exception {
        try (MockedStatic<GenreMapper> mockedStatic = Mockito.mockStatic(GenreMapper.class)) {

            when(genreService.update(genreDTO.getId(), genreDTO.getName()))
                    .thenReturn(genre);
            mockedStatic.when(() -> GenreMapper.toGenreDto(genre))
                    .thenReturn(genreDTO);

            mvc.perform(put("/api/v1/genres/{id}", genreDTO.getId())
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(genreDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(genre.getId()))
                    .andExpect(jsonPath("$.name").value(genre.getName()));

            verify(genreService).update(genreDTO.getId(), genreDTO.getName());
        }
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе PUT /api/v1/genres/{id} с некорректными данными - должен вернуть статус ошибки")
    void testUpdateGenre_withValidationErrors() throws Exception {

        genreDTO.setName("");

        mvc.perform(put("/api/v1/genres/{id}", genreDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(genreDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("при запросе DELETE /genres/{id} - должен удалить жанр")
    void testDeleteGenre() throws Exception {

        mvc.perform(delete("/api/v1/genres/{id}", genreDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(genreService).deleteById(genreDTO.getId());
    }

    @Test
    @DisplayName("при запросе GET /api/v1/genres без аутентификации - должен вернуть 401")
    void testGetGenresUnauthorized() throws Exception {
        mvc.perform(get("/api/v1/genres"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе GET /api/v1/genres/{id} без аутентификации - должен вернуть 401")
    void testGetGenreUnauthorized() throws Exception {
        mvc.perform(get("/api/v1/genres/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе POST /api/v1/genres без аутентификации - должен вернуть 401")
    void testCreateGenreUnauthorized() throws Exception {
        mvc.perform(post("/api/v1/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(genreDTO))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе PUT /api/v1/genres/{id} без аутентификации - должен вернуть 401")
    void testUpdateGenreUnauthorized() throws Exception {
        mvc.perform(put("/api/v1/genres/{id}", genreDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(genreDTO))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("при запросе DELETE /api/v1/genres/{id} без аутентификации - должен вернуть 401")
    void testDeleteGenreUnauthorized() throws Exception {
        mvc.perform(delete("/api/v1/genres/{id}", genreDTO.getId())
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

}
