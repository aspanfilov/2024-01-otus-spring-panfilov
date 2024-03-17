package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityInsertException;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcOperations jdbcOperations;
    private static final RowMapper<Genre> GENRE_ROW_MAPPER = new JdbcGenreRepository.GenreRowMapper();

    @Override
    public List<Genre> findAll() {
        return jdbcOperations.query("SELECT id, name FROM genres", GENRE_ROW_MAPPER);
    }

    @Override
    public List<Genre> findAllByIds(Set<Long> ids) {
        return jdbcOperations.query("SELECT id, name FROM genres WHERE id IN :ids",
                Map.of("ids", ids), GENRE_ROW_MAPPER);
    }

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == 0) {
            return insert(genre);
        }
        return update(genre);
    }

    @Override
    public void deleteById(long id) {
        jdbcOperations.update("DELETE FROM genres WHERE id = :id", Map.of("id", id));
    }

    private Genre insert(Genre genre) {
        String sql = "INSERT INTO genres (name) VALUES (:name)";
        var params = new MapSqlParameterSource()
                .addValue("name", genre.getName());
        var keyHolder = new GeneratedKeyHolder();

        jdbcOperations.update(sql, params, keyHolder, new String[]{"id"});

        var key = keyHolder.getKey();
        if (key == null) {
            throw new EntityInsertException("KeyHolder did not generate a key for the book insert.");
        }
        genre.setId(key.longValue());

        return genre;
    }

    private Genre update(Genre genre) {
        int updateCount = jdbcOperations.update("UPDATE genres set name = :name WHERE id = :id",
                Map.of("id", genre.getId(), "name", genre.getName()));

        if (updateCount == 0) {
            throw new EntityNotFoundException("The genre with ID " + genre.getId() + " was not found.");
        }

        return genre;
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
