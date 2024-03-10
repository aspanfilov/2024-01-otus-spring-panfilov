package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations jdbcOperations;
    private static final RowMapper<Author> AUTHOR_ROW_MAPPER = new AuthorRowMapper();

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

    private static class AuthorRowMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            var id = rs.getLong("id");
            var fullName = rs.getString("fullName");
            return new Author(id, fullName);
        }
    }
}
