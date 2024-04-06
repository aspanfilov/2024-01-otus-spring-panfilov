package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityInsertException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private static final RowMapper<Author> AUTHOR_ROW_MAPPER = new AuthorRowMapper();

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Author> findAll() {
        return jdbcOperations.query("SELECT id, full_name FROM authors", AUTHOR_ROW_MAPPER);
    }

    @Override
    public Optional<Author> findById(long id) {
        return jdbcOperations.query("SELECT id, full_name FROM authors WHERE id = :id",
                        Map.of("id", id), AUTHOR_ROW_MAPPER)
                .stream()
                .findFirst();
    }

    @Override
    public Author save(Author author) {
        if (author.getId() == 0) {
            return insert(author);
        }
        return update(author);
    }

    @Override
    public void deleteById(long id) {
        jdbcOperations.update("DELETE FROM authors WHERE id = :id", Map.of("id", id));
    }

    private Author insert(Author author) {
        String sql = "INSERT INTO authors (full_name) VALUES (:fullName)";
        var params = new MapSqlParameterSource()
                .addValue("fullName", author.getFullName());
        var keyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(sql, params, keyHolder, new String[]{"id"});

        var key = keyHolder.getKey();
        if (key == null) {
            throw new EntityInsertException("KeyHolder did not generate a key for the book insert.");
        }
        author.setId(key.longValue());

        return author;
    }

    private Author update(Author author) {
        int updateCount = jdbcOperations.update("UPDATE authors set full_name = :fullName WHERE id = :id",
                Map.of("id", author.getId(), "fullName", author.getFullName()));

        if (updateCount == 0) {
            throw new EntityNotFoundException("The author with ID " + author.getId() + " was not found.");
        }

        return author;
    }

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            var id = rs.getLong("id");
            var fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
