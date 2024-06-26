package ru.otus.hw.controllers.mvc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.GenreRepository;

@Controller
@RequiredArgsConstructor
public class BookPageController {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    @GetMapping("/books")
    public String booksListPage() {
        return "books/list";
    }

    @GetMapping("/books/new")
    public String newBookPage(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "books/new";
    }

    @GetMapping("/books/{id}")
    public String bookPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("bookId", id);
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("genres", genreRepository.findAll());
        return "books/edit";
    }

}
