package ru.otus.hw.models.sql;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books_genres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SqlBooksGenresId.class)
public class SqlBooksGenres {
    @Id
    @Column(name = "book_id")
    private long bookId;

    @Id
    @Column(name = "genre_id")
    private long genreId;
}
