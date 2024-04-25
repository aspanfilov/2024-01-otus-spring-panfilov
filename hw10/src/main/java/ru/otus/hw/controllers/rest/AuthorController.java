package ru.otus.hw.controllers.rest;

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
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.services.AuthorService;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors")
    public String listAuthors(Model model) {
        var authors = authorService.findAll()
                .stream().map(AuthorMapper::toAuthorDto)
                .toList();
        model.addAttribute("authors", authors);

        return "authors/list";
    }

    @GetMapping("/authors/new")
    public String newAuthor(@ModelAttribute("author") AuthorDTO author) {
        return "authors/new";
    }

    @PostMapping("/authors")
    public String createAuthor(@Valid @ModelAttribute("author") AuthorDTO author,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "authors/new";
        }
        authorService.insert(author.getFullName());
        return "redirect:/authors";
    }

    @GetMapping("/authors/{id}")
    public String editAuthor(@PathVariable("id") Long id, Model model) {
        var author = authorService.findById(id)
                .map(AuthorMapper::toAuthorDto)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(id)));
        model.addAttribute("author", author);
        return "authors/edit";
    }

    @PatchMapping("/authors")
    public String updateAuthor(@Valid @ModelAttribute("author") AuthorDTO author,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "authors/edit";
        }
        authorService.update(author.getId(), author.getFullName());
        return "redirect:/authors";
    }

    @DeleteMapping("/authors/{id}")
    public String delete(@PathVariable("id") long id) {
        authorService.deleteById(id);
        return "redirect:/authors";
    }
}
