package ru.otus.hw.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.spi.Readable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.book.BookDetailDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    private static final String SQL_BOOK_BY_ID = """
            SELECT
                b.id,
                b.title,
                b.author_id,
                a.full_name as author_full_name
            FROM books b
            LEFT JOIN authors a on b.author_id = a.id
            WHERE b.id = $1""";

    private static final String SQL_GENRES_BY_BOOK_ID = """
            SELECT
                g.id,
                g.name
            FROM books_genres bg
                INNER JOIN genres g ON bg.genre_id = g.id
            WHERE bg.book_id = $1""";

    private static final String SQL_ALL_BOOKS = """
            SELECT
                b.id,
                b.title,
                b.author_id,
                a.full_name as author_full_name,
                json_agg(g.name) as genre_names
            FROM books b
            LEFT JOIN authors a on b.author_id = a.id
            LEFT JOIN books_genres bg ON b.id = bg.book_id
                INNER JOIN genres g ON bg.genre_id = g.id
            GROUP BY b.id, b.title, b.author_id, a.full_name""";

    private final R2dbcEntityOperations template;

    private final ObjectMapper objectMapper;

    @Override
    public Flux<BookDetailDTO> findAll() {
        return template.getDatabaseClient().inConnectionMany(connection ->
                Flux.from(connection.createStatement(SQL_ALL_BOOKS).execute())
                        .flatMap(result -> result.map(this::mapToBookDetailDto)));
    }

    @Override
    public Mono<Book> findById(Long id) {
        return Mono.zip(
                fetchBookById(id),
                fetchGenresForBook(id),
                (book, genres) -> {
                    book.setGenres(genres);
                    return book;
                });
    }

    private Mono<Book> fetchBookById(Long id) {
        return template.getDatabaseClient().inConnection(connection ->
                Mono.from(connection.createStatement(SQL_BOOK_BY_ID)
                                .bind(0, id)
                                .execute())
                        .flatMapMany(result -> result.map(this::mapToBook))
                        .next());
    }

    private Mono<List<Genre>> fetchGenresForBook(Long bookId) {
        return template.getDatabaseClient().inConnection(connection ->
                Mono.from(connection.createStatement(SQL_GENRES_BY_BOOK_ID)
                                .bind(0, bookId)
                                .execute())
                        .flatMapMany(result -> result.map(this::mapToGenre))
                        .collectList());
    }

    private BookDetailDTO mapToBookDetailDto(Readable selectedRecord) {
        var bookGenresAsText = selectedRecord.get("genre_names", String.class);
        try {
            List<String> bookGenres = objectMapper.readValue(bookGenresAsText, new TypeReference<>() {
            });
            return BookDetailDTO.builder()
                    .id(selectedRecord.get("id", Long.class))
                    .title(selectedRecord.get("title", String.class))
                    .author(selectedRecord.get("author_full_name", String.class))
                    .genres(bookGenres)
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("genres:" + bookGenresAsText + " parsing error:" + e);
        }
    }

    private Book mapToBook(Readable selectedRecord) {
        return new Book(
                selectedRecord.get("id", Long.class),
                selectedRecord.get("title", String.class),
                selectedRecord.get("author_id", Long.class),
                Author.builder()
                        .id(selectedRecord.get("author_id", Long.class))
                        .fullName(selectedRecord.get("author_full_name", String.class))
                        .build());
    }

    private Genre mapToGenre(Readable row) {
        return new Genre(
                row.get("id", Long.class),
                row.get("name", String.class)
        );
    }
}
