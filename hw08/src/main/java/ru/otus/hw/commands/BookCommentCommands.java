package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.BookCommentConverter;
import ru.otus.hw.services.BookCommentService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommentCommands {

    private final BookCommentService bookCommentService;

    private final BookCommentConverter bookCommentConverter;

    @ShellMethod(value = "Find all book comments by book id", key = "bcbb")
    public String findBookCommentsByBookId(String id) {
        return bookCommentService.findAllByBookId(id).stream()
                .map(bookCommentConverter::bookCommentToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book comments by id", key = "bcbid")
    public String findBookCommentById(String id) {
        return bookCommentService.findById(id)
                .map(bookCommentConverter::bookCommentToString)
                .orElse("Book comment with id %s not found".formatted(id));
    }

    @ShellMethod(value = "Insert book comments", key = "bcins")
    public String insertBookComment(String bookId, String commentText) {
        var savedBookComment = bookCommentService.insert(bookId, commentText);
        return bookCommentConverter.bookCommentToString(savedBookComment);
    }

    @ShellMethod(value = "Update book comments", key = "bcupd")
    public String updateBookComment(String id, String commentText, String bookId) {
        var savedBookComment = bookCommentService.update(id, bookId, commentText);
        return bookCommentConverter.bookCommentToString(savedBookComment);
    }

    @ShellMethod(value = "Delete book comments by id", key = "bcdel")
    public void deleteAuthor(String id) {
        bookCommentService.deleteById(id);
    }
}
