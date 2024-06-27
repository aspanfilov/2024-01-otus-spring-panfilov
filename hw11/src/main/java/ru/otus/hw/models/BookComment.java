package ru.otus.hw.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("book_comments")
@Getter
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

    @PersistenceCreator
    public BookComment(@JsonProperty("id") Long id,
                       @JsonProperty("commentText") String commentText,
                       @JsonProperty("bookId") Long bookId) {
        this.id = id;
        this.commentText = commentText;
        this.bookId = bookId;
    }

}
