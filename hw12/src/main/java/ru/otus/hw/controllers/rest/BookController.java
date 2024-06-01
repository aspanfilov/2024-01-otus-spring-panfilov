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
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.BookRequestDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<BookDTO> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public BookDTO getBook(@PathVariable("id") Long id) {
        return bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
    }

    @PostMapping("/api/v1/books")
    public BookDTO createBook(@RequestBody @Valid BookRequestDTO bookRequest) {
        return bookService.insert(bookRequest.getTitle(),
                bookRequest.getAuthorId(),
                bookRequest.getGenreIds());
    }

    @PutMapping("/api/v1/books/{id}")
    public BookDTO updateBook(@RequestBody @Valid BookRequestDTO bookRequest) {
        return bookService.update(bookRequest.getId(),
                bookRequest.getTitle(),
                bookRequest.getAuthorId(),
                bookRequest.getGenreIds());
    }

    @DeleteMapping("/api/v1/books/{id}")
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }

}
