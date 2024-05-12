package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.BookComment;

@Component
@RequiredArgsConstructor
public class BookCommentConverter {
    private final BookConverter bookConverter;

    public String bookCommentToString(BookComment bookComment) {
        return "Id: %d, commentText: %s, book: {%s}".formatted(
                bookComment.getId(),
                bookComment.getCommentText(),
                bookConverter.bookToString(bookComment.getBook()));
    }
}
