package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class MongoGenreCascadeDeleteListener extends AbstractMongoEventListener<Genre> {

    private final BookRepository bookRepository;

    public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
        super.onBeforeDelete(event);

        var genreSource = event.getSource();
        if (bookRepository.existsByGenresId(genreSource.get("_id").toString())) {
            throw new IllegalStateException("Genre cannot be deleted as it is referenced by a book.");
        }
    }
}
