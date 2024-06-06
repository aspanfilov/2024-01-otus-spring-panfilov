package ru.otus.hw.events;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.BookCommentRepository;

@Component
@RequiredArgsConstructor
public class MongoBookCascadeDeleteListener extends AbstractMongoEventListener<Book> {

    private final BookCommentRepository bookCommentRepository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Book> event) {
        super.onAfterDelete(event);

        var bookSource = event.getSource();
        var bookId = bookSource.get("_id").toString();
        bookCommentRepository.deleteAllByBookId(bookId);
    }

}
