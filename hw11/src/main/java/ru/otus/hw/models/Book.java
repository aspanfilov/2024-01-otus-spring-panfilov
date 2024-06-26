package ru.otus.hw.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
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
    //todo пересмотреть состав модели (удалить transient?)
    //todo также для всех моделей ненужны setter-ы так как поля final

    @Id
    @Column(value = "id")
    private final Long id;

    @Column(value = "title")
    @NotNull
    private final String title;

    @Column("author_id")
    @NotNull
    private final Long authorId;

    @Transient
    private final Author author;

    @Transient
    private final List<BookGenreRef> bookGenreRefs;

    @Transient
    private final List<Genre> genres;

    @Transient
    private final List<String> genresNames;

    @PersistenceCreator
    public Book(Long id, @NotNull String title, @NotNull Long authorId) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.author = null;
        this.bookGenreRefs = null;
        this.genres = null;
        this.genresNames = null;
    }
}
