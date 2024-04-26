package ru.otus.hw.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AuthorPageController {

    @GetMapping("/authors")
    public String authorsListPage() {
        return "authors/list";
    }

    @GetMapping("/authors/new")
    public String newAuthorPage() {
        return "authors/new";
    }

    @GetMapping("/authors/{id}")
    public String authorPage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("authorId", id);
        return "authors/edit";
    }

}
