package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
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

//    @GetMapping("/genres")
//    public String listGenres(Model model) {
//        var genres = genreService.findAll()
//                .stream().map(GenreMapper::toGenreDto)
//                .toList();
//        model.addAttribute("genres", genres);
//
//        return "genres/list";
//    }

    @GetMapping("/api/v1/genres")
    public List<GenreDTO> getGenres() {
        return genreService.findAll().stream()
                .map(GenreMapper::toGenreDto)
                .toList();
    }

//    @GetMapping("/genres/{id}")
//    public String editGenre(@PathVariable("id") Long id, Model model) {
//        var genre = genreService.findById(id)
//                .map(GenreMapper::toGenreDto)
//                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(id)));
//        model.addAttribute("genre", genre);
//        return "genres/edit";
//    }

    @GetMapping("/api/v1/genres/{id}")
    public GenreDTO getGenre(@PathVariable("id") Long id) {
        return genreService.findById(id)
                .map(GenreMapper::toGenreDto)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(id)));
    }

//    @GetMapping("/genres/new")
//    public String newGenre(@ModelAttribute("genre") GenreDTO genre) {
//        return "genres/new";
//    }
//
//    @PostMapping("/genres")
//    public String createGenre(@Valid @ModelAttribute("genre") GenreDTO genre,
//                              BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "genres/new";
//        }
//        genreService.insert(genre.getName());
//        return "redirect:/genres";
//    }

    @PostMapping("/api/v1/genres")
    public GenreDTO createGenre(@RequestBody @Valid GenreDTO genreDTO) {
        Genre genre = genreService.insert(genreDTO.getName());
        return GenreMapper.toGenreDto(genre);
    }

//    @PatchMapping("/genres")
//    public String updateGenre(@Valid @ModelAttribute("genre") GenreDTO genre,
//                              BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "genres/edit";
//        }
//        genreService.update(genre.getId(), genre.getName());
//        return "redirect:/genres";
//    }

    @PutMapping("/api/v1/genres/{id}")
    public GenreDTO updateGenre(@PathVariable("id") Long id,
                                @RequestBody @Valid GenreDTO genreDTO) {
        Genre genre = genreService.update(id, genreDTO.getName());
        return GenreMapper.toGenreDto(genre);
    }

//    @DeleteMapping("/genres/{id}")
//    public String delete(@PathVariable("id") long id) {
//        genreService.deleteById(id);
//        return "redirect:/genres";
//    }

    @DeleteMapping("/api/v1/genres/{id}")
    public void deleteGenre(@PathVariable("id") Long id) {
        genreService.deleteById(id);
    }

}
