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
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/api/v1/authors")
    public List<AuthorDTO> getAuthors() {
        return authorService.findAll().stream()
                .map(AuthorMapper::toAuthorDto)
                .toList();
    }

    @GetMapping("/api/v1/authors/{id}")
    public AuthorDTO getAuthor(@PathVariable("id") Long id) {
        return authorService.findById(id)
                .map(AuthorMapper::toAuthorDto)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(id)));
    }

    @PostMapping("/api/v1/authors")
    public AuthorDTO createAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
        Author author = authorService.insert(authorDTO.getFullName());
        return AuthorMapper.toAuthorDto(author);
    }

    @PutMapping("/api/v1/authors/{id}")
    public AuthorDTO updateAuthor(@PathVariable("id") Long id,
                                  @RequestBody @Valid AuthorDTO authorDTO) {
        Author author = authorService.update(id, authorDTO.getFullName());
        return AuthorMapper.toAuthorDto(author);
    }

    @DeleteMapping("/api/v1/authors/{id}")
    public void deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteById(id);
    }

}
