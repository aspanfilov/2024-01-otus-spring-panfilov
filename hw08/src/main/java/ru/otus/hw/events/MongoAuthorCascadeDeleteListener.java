package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class MongoAuthorCascadeDeleteListener extends AbstractMongoEventListener<Author> {

    private final BookRepository bookRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
        super.onBeforeDelete(event);

        var authorSource = event.getSource();
        if (bookRepository.existsByAuthorId(authorSource.get("_id").toString())) {
            throw new IllegalStateException("Author cannot be deleted as it is referenced by a book.");
        }
    }
}
