package ru.otus.hw.mappers;

import ru.otus.hw.dtos.BookCommentDTO;
import ru.otus.hw.models.BookComment;

public class BookCommentMapper {
    public static BookCommentDTO toBookCommentDTO(BookComment bookComment) {
        return BookCommentDTO.builder()
                .id(bookComment.getId())
                .commentText(bookComment.getCommentText())
                .book(BookMapper.toBookDTO(bookComment.getBook()))
                .user(UserMapper.toUserViewDTO(bookComment.getUser()))
                .build();
    }
}
