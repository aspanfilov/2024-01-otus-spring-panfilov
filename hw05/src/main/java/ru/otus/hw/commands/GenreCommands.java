package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.GenreConverter;
import ru.otus.hw.services.GenreService;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class GenreCommands {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find all genres by ids", key = "agbid")
    public String findAllGenresByIds(Set<Long> ids) {
        return genreService.findAllByIds(ids).stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find genre by id", key = "gbid")
    public String findGenreById(long id) {
        return genreService.findById(id)
                .map(genreConverter::genreToString)
                .orElse("Genre with id %d not found".formatted(id));
    }

    // gins newGenre
    @ShellMethod(value = "Insert genre", key = "gins")
    public String insertGenre(String fullName) {
        var savedGenre = genreService.insert(fullName);
        return genreConverter.genreToString(savedGenre);
    }

    // gupd 3, editedGenre
    @ShellMethod(value = "Update genre", key = "gupd")
    public String updateGenre(long id, String fullName) {
        var savedGenre = genreService.update(id, fullName);
        return genreConverter.genreToString(savedGenre);
    }

    // gdel 2
    @ShellMethod(value = "Delete genre by id", key = "gdel")
    public void deleteGenre(long id) {
        genreService.deleteById(id);
    }
}
