package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookCommentConverter;
import ru.otus.hw.dtos.BookCommentDTO;
import ru.otus.hw.services.BookCommentService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommentCommands {

    private final BookCommentService bookCommentService;

    private final BookCommentConverter bookCommentConverter;

    @ShellMethod(value = "Find all book comments by book id", key = "bcbb")
    public String findBookCommentsByBookId(long id) {
        return bookCommentService.findAllByBookId(id).stream()
                .map(BookCommentDTO::toString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book comments by id", key = "bcbid")
    public String findBookCommentById(long id) {
        return bookCommentService.findById(id)
                .map(BookCommentDTO::toString)
                .orElse("Book comment with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Insert book comments", key = "bcins")
    public String insertBookComment(long bookId, String commentText) {
        var savedBookCommentDTO = bookCommentService.insert(bookId, commentText);
        return savedBookCommentDTO.toString();
    }

    @ShellMethod(value = "Update book comments", key = "bcupd")
    public String updateBookComment(long id, String commentText, long bookId) {
        var savedBookCommentDTO = bookCommentService.update(id, bookId, commentText);
        return savedBookCommentDTO.toString();
    }

    @ShellMethod(value = "Delete book comments by id", key = "bcdel")
    public void deleteAuthor(long id) {
        bookCommentService.deleteById(id);
    }
}
