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
import ru.otus.hw.dtos.BookCommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookCommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookCommentController {

    private final BookCommentService bookCommentService;

    @GetMapping("/api/v1/books/{bookId}/comments")
    public List<BookCommentDTO> getBookComments(@PathVariable("bookId") Long bookId) {
        return bookCommentService.findAllByBookId(bookId);
    }

    @GetMapping("/api/v1/books/{bookid}/comments/{bookCommentId}")
    public BookCommentDTO getBookComment(@PathVariable("bookCommentId") Long bookCommentId) {
        return bookCommentService.findById(bookCommentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book comment with id %d not found".formatted(bookCommentId)));
    }

    @PostMapping("/api/v1/books/{bookId}/comments")
    public BookCommentDTO createBookComment(@PathVariable("bookId") Long bookId,
                                            @RequestBody @Valid BookCommentDTO bookCommentDTO) {
        return bookCommentService.insert(bookCommentDTO);
    }

    @PutMapping("/api/v1/books/{bookId}/comments/{bookCommentId}")
    public BookCommentDTO updateBookComment(@PathVariable("bookId") Long bookId,
                                            @PathVariable("bookCommentId") Long bookCommentId,
                                            @RequestBody @Valid BookCommentDTO bookCommentDTO) {
        return bookCommentService.update(bookCommentId, bookCommentDTO);
    }

    @DeleteMapping("/api/v1/books/{bookId}/comments/{bookCommentId}")
    public void deleteBookComment(@PathVariable("bookId") Long bookId,
                                  @PathVariable("bookCommentId") Long bookCommentId) {
        bookCommentService.deleteById(bookCommentId);
    }
}
