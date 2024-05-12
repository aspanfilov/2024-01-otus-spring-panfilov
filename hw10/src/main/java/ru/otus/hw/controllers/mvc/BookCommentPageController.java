package ru.otus.hw.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookCommentPageController {

    @GetMapping("/books/{bookId}/comments")
    public String bookCommentsListPage(@PathVariable("bookId") Long bookId,
                                       Model model) {
        model.addAttribute("bookId", bookId);
        return "books/comments/list";
    }

    @GetMapping("/books/{bookId}/comments/new")
    public String newBookCommentPage(@PathVariable("bookId") Long bookId,
                                     Model model) {
        model.addAttribute("bookId", bookId);
        return "books/comments/new";
    }

    @GetMapping("/books/{bookId}/comments/{bookCommentId}")
    public String bookCommentPage(@PathVariable("bookId") Long bookId,
                                  @PathVariable("bookCommentId") Long bookCommentId,
                                  Model model) {
        model.addAttribute("bookId", bookId);
        model.addAttribute("bookCommentId", bookCommentId);
        return "books/comments/edit";
    }

}
