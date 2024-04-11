package ru.otus.hw.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "book_comments")
@NamedEntityGraph(
        name = "bookComment-book-entity-graph",
        attributeNodes = {@NamedAttributeNode(value = "book", subgraph = "book-author-subgraph")},
        subgraphs = {@NamedSubgraph(
                name = "book-author-subgraph",
                attributeNodes = {@NamedAttributeNode("author")})
        }
)
@Getter
@Setter
@EqualsAndHashCode(exclude = {"book"})
@ToString(exclude = {"book"})
@NoArgsConstructor
@AllArgsConstructor
public class BookComment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "comment_text", unique = false, nullable = false)
    private String commentText;

    @ManyToOne(cascade = {},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
