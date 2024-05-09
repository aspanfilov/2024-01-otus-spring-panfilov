package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.otus.hw.dtos.BookRequestDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    @GetMapping("/books")
    public String listBooks(Model model) {
        var books = bookService.findAll();
        model.addAttribute("books", books);

        return "books/list";
    }

    @GetMapping("/books/new")
    public String newBook(@ModelAttribute("bookRequest") BookRequestDTO bookRequest,
                          Model model) {
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());

        return "books/new";
    }

    @PostMapping("/books")
    public String createBook(@Valid @ModelAttribute("bookRequest") BookRequestDTO bookRequest,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "books/new";
        }

        bookService.insert(bookRequest.getTitle(),
                bookRequest.getAuthorId(),
                bookRequest.getGenreIds());

        return "redirect:/books";
    }

    @GetMapping("/books/{id}")
    public String editBook(@PathVariable("id") Long id, Model model) {
        var bookRequest = bookService.findBookById(id)
                .map(BookMapper::toBookRequestDTO)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        model.addAttribute("bookRequest", bookRequest);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());

        return "books/edit";
    }

    @PutMapping("/books")
    public String updateBook(@Valid @ModelAttribute("bookRequest") BookRequestDTO bookRequest,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.findAll());
            model.addAttribute("genres", genreService.findAll());
            return "books/edit";
        }

        bookService.update(bookRequest.getId(),
                bookRequest.getTitle(),
                bookRequest.getAuthorId(),
                bookRequest.getGenreIds());

        return "redirect:/books";
    }

    @DeleteMapping("/books/{id}")
    public String delete(@PathVariable("id") long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

}
