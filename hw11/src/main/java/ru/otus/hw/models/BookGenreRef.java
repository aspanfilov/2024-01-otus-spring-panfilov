package ru.otus.hw.models;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("books_genres")
@Data
public class BookGenreRef {

    @Column("book_id")
    @NotNull
    private final Long bookId;

    @Column("genre_id")
    @NotNull
    private final Long genreId;

    @PersistenceCreator
    public BookGenreRef(Long bookId, Long genreId) {
        this.bookId = bookId;
        this.genreId = genreId;
    }
}
