package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
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

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            var id = rs.getLong("id");
            var name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
