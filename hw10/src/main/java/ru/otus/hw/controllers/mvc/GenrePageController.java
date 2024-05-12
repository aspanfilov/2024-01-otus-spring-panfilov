package ru.otus.hw.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GenrePageController {

    @GetMapping("/genres")
    public String genresListPage() {
        return "genres/list";
    }

    @GetMapping("/genres/new")
    public String newGenrePage() {
        return "genres/new";
    }

    @GetMapping("/genres/{id}")
    public String genrePage(@PathVariable("id") Long id, Model model) {
        model.addAttribute("genreId", id);
        return "genres/edit";
    }

}
