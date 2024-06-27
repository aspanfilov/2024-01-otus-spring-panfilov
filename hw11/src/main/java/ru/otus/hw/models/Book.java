package ru.otus.hw.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
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
@Builder
public class Book {

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
    @Setter
    private List<Genre> genres;

    @PersistenceCreator
    public Book(@JsonProperty("id") Long id,
                @JsonProperty("title") @NotNull String title,
                @JsonProperty("authorId") @NotNull Long authorId) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.author = null;
        this.genres = null;
    }

    @JsonCreator
    public Book(@JsonProperty("id") Long id,
                @JsonProperty("title") @NotNull String title,
                @JsonProperty("authorId") @NotNull Long authorId,
                @JsonProperty("author") Author author,
                @JsonProperty("genres") List<Genre> genres) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.author = author;
        this.genres = genres;
    }

}
