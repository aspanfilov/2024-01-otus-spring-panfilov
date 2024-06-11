package ru.otus.hw.models.sql;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqlBook {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", unique = false, nullable = false)
    private String title;

    @Column(name = "author_id", nullable = false)
    private long authorId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "books_genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre_id")
    private List<Long> genreIds;

}
