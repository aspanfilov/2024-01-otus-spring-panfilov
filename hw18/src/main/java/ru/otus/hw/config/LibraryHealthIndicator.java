package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LibraryHealthIndicator implements HealthIndicator {

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookService bookService;

    @Override
    public Health health() {
        long authorCount = authorService.getCount();
        long genreCount = genreService.getCount();
        long bookCount = bookService.getCount();

        Map<String, Object> details = new HashMap<>();

        addCountDetails(details, "Authors", authorCount);
        addCountDetails(details, "Genres", genreCount);
        addCountDetails(details, "Books", bookCount);

        boolean hasErrors = authorCount == 0 || genreCount == 0 || bookCount == 0;

        return (hasErrors ? Health.down() : Health.up())
                .withDetails(details)
                .build();
    }

    private void addCountDetails(Map<String, Object> details, String entityName, long count) {
        if (count == 0) {
            details.put("Error", "No " + entityName.toLowerCase() + " found");
        } else {
            details.put(entityName, count);
        }
    }

}
