package ru.otus.hw.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table(name = "books")
@Getter
@EqualsAndHashCode(exclude = {"author", "genres"})
@ToString(exclude = {"author", "genres"})
public class Book {

    @Id
    @Column(value = "id")
    private final Long id;

    @Column(value = "title")
    @NotNull
    private final String title;

    //todo попробовать удалить это поле
    @Column("author_id")
    @NotNull
    private final Long authorId;

    @Transient
    private final Author author;

    @Transient
    @Setter
    private List<Genre> genres;

    @PersistenceCreator
    public Book(Long id, @NotNull String title, @NotNull Long authorId) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.author = null;
        this.genres = null;
    }

    public Book(Long id, @NotNull String title, @NotNull Long authorId, Author author) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.author = author;
        this.genres = null;
    }

}
