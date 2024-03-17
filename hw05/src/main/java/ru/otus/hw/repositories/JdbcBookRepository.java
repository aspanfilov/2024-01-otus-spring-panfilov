package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityInsertException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbcOperations;
    private final GenreRepository genreRepository;

    private static final RowMapper<Book> BOOK_ROW_MAPPER =
            new JdbcBookRepository.BookRowMapper();
    private static final ResultSetExtractor<Book> BOOK_RESULT_SET_EXTRACTOR =
            new JdbcBookRepository.BookResultSetExtractor();

    @Override
    public Optional<Book> findById(long id) {
        String sql = """
                SELECT b.id        AS book_id,
                       b.title     AS book_title,
                       a.id        AS author_id,
                       a.full_name AS author_fullname,
                       g.id        AS genre_id,
                       g.name      AS genre_name
                FROM books b
                    LEFT JOIN authors a ON b.author_id = a.id
                    LEFT JOIN books_genres bg ON b.id = bg.book_id
                    LEFT JOIN genres g ON g.id = bg.genre_id
                WHERE b.id = :id""";
        return Optional.ofNullable(jdbcOperations.query(sql, Map.of("id", id), BOOK_RESULT_SET_EXTRACTOR));
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbcOperations.update("DELETE FROM books WHERE id = :id", Map.of("id", id));
    }

    private List<Book> getAllBooksWithoutGenres() {
        String sql = """
                SELECT b.id        AS book_id,
                       b.title     AS book_title,
                       b.author_id AS author_id,
                       a.full_name AS author_fullname
                FROM books AS b
                    LEFT JOIN authors AS a ON b.author_id = a.id""";
        return jdbcOperations.query(sql, BOOK_ROW_MAPPER);
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbcOperations.query("SELECT book_id, genre_id FROM books_genres",
                (rs, rowNum) -> new BookGenreRelation(
                        rs.getLong("book_id"),
                        rs.getLong("genre_id")));
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        booksWithoutGenres.forEach(book -> {
            var bookGenreIds = relations.stream()
                    .filter(relation -> relation.bookId() == book.getId())
                    .map(BookGenreRelation::genreId)
                    .toList();
            var bookGenres = genres.stream()
                    .filter(genre -> bookGenreIds.contains(genre.getId()))
                    .toList();
            book.setGenres(bookGenres);
        });
    }

    private Book insert(Book book) {
        String sql = "INSERT INTO books (title, author_id) VALUES (:title, :authorId)";
        var params = createBookParameters(book);
        var keyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(sql, params, keyHolder, new String[]{"id"});

        var key = keyHolder.getKey();
        if (key == null) {
            throw new EntityInsertException("KeyHolder did not generate a key for the book insert.");
        }
        book.setId(key.longValue());

//        noinspection DataFlowIssue
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {
        String sql = "UPDATE books SET title = :title, author_id = :authorId WHERE id = :id";
        var params = createBookParameters(book);

        int updateCount = jdbcOperations.update(sql, params);

        if (updateCount == 0) {
            throw new EntityNotFoundException("The book with ID " + book.getId() + " was not found.");
        }
        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private MapSqlParameterSource createBookParameters(Book book) {
        var params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("authorId", book.getAuthor() == null ? null : book.getAuthor().getId());
        if (book.getId() != 0) {
            params.addValue("id", book.getId());
        }
        return params;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        @SuppressWarnings("unchecked")
        Map<String, Object>[] batchValues = book.getGenres().stream()
                .map(genre -> Map.of(
                        "bookId", book.getId(),
                        "genreId", genre.getId()))
                .toArray(Map[]::new);
        jdbcOperations.batchUpdate("INSERT INTO books_genres (book_id, genre_id) VALUES (:bookId, :genreId)",
                batchValues);
    }

    private void removeGenresRelationsFor(Book book) {
        jdbcOperations.update("DELETE FROM books_genres WHERE book_id = :bookId",
                Map.of("bookId", book.getId()));
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            var id = rs.getLong("id");
            var title = rs.getString("title");
            var authorId = rs.getLong("author_id");
            var authorFullName = rs.getString("author_fullname");
            var author = new Author(authorId, authorFullName);
            return new Book(id, title, author, Collections.emptyList());
        }
    }

    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.next()) {
                return null;
            }
            var book = new Book();
            book.setId(rs.getLong("book_id"));
            book.setTitle(rs.getString("book_title"));

            var authorId = rs.getObject("author_id", Long.class);
            var authorFullName = rs.getString("author_fullname");
            if (authorId != null) {
                book.setAuthor(new Author(authorId, authorFullName));
            }

            List<Genre> genres = new ArrayList<>();
            do {
                var genreId = rs.getObject("genre_id", Long.class);
                var genreName = rs.getString("genre_name");
                if (genreId != null) {
                    genres.add(new Genre(genreId, genreName));
                }
            } while (rs.next());

            book.setGenres(genres);
            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}
