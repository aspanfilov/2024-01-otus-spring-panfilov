package ru.otus.hw.mappers;

import ru.otus.hw.dtos.BookCommentDTO;
import ru.otus.hw.models.BookComment;

public class BookCommentMapper {
    public static BookComment toNewEntity(BookCommentDTO bookCommentDTO) {
        return BookComment.builder()
                .id(null)
                .commentText(bookCommentDTO.getCommentText())
                .bookId(bookCommentDTO.getBookId())
                .build();
    }

    public static BookComment toEntity(BookCommentDTO bookCommentDTO) {
        return BookComment.builder()
                .id(bookCommentDTO.getId())
                .commentText(bookCommentDTO.getCommentText())
                .bookId(bookCommentDTO.getBookId())
                .build();
    }
}
