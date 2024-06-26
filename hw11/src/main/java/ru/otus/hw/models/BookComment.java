package ru.otus.hw.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("book_comments")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"book"})
@ToString(exclude = {"book"})
public class BookComment {
    @Id
    @Column("id")
    private final Long id;

    @Column("comment_text")
    @NotNull
    private final String commentText;

    @Column("book_id")
    @NotNull
    private final Long bookId;

//    @Transient
//    private final Book book;

    @PersistenceCreator
    public BookComment(Long id, String commentText, Long bookId) {
        this.id = id;
        this.commentText = commentText;
        this.bookId = bookId;
//        this.book = null;
    }

}
