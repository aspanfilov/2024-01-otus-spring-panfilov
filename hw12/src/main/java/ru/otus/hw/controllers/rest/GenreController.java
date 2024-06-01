package ru.otus.hw.controllers.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/api/v1/genres")
    public List<GenreDTO> getGenres() {
        return genreService.findAll().stream()
                .map(GenreMapper::toGenreDto)
                .toList();
    }

    @GetMapping("/api/v1/genres/{id}")
    public GenreDTO getGenre(@PathVariable("id") Long id) {
        return genreService.findById(id)
                .map(GenreMapper::toGenreDto)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(id)));
    }

    @PostMapping("/api/v1/genres")
    public GenreDTO createGenre(@RequestBody @Valid GenreDTO genreDTO) {
        Genre genre = genreService.insert(genreDTO.getName());
        return GenreMapper.toGenreDto(genre);
    }

    @PutMapping("/api/v1/genres/{id}")
    public GenreDTO updateGenre(@PathVariable("id") Long id,
                                @RequestBody @Valid GenreDTO genreDTO) {
        Genre genre = genreService.update(id, genreDTO.getName());
        return GenreMapper.toGenreDto(genre);
    }

    @DeleteMapping("/api/v1/genres/{id}")
    public void deleteGenre(@PathVariable("id") Long id) {
        genreService.deleteById(id);
    }

}
