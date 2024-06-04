package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.BookComment;

@Component
@RequiredArgsConstructor
public class BookCommentConverter {
    private final BookConverter bookConverter;

    public String bookCommentToString(BookComment bookComment) {
        return "Id: %s, commentText: %s, book: %s".formatted(
                bookComment.getId(),
                bookComment.getCommentText(),
                bookComment.getBook().getId());
//                bookConverter.bookToString(bookComment.getBook()));
    }
}
