package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LibraryHealthIndicator implements HealthIndicator {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        long authorCount = authorRepository.count();
        long genreCount = genreRepository.count();
        long bookCount = bookRepository.count();

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
