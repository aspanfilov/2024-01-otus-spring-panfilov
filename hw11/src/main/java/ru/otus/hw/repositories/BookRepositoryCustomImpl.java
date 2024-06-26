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
import ru.otus.hw.dtos.BookDetailDTO;
import ru.otus.hw.dtos.BookReferenceDTO;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    //todo требуется рефакторинг запросу - удалить соединение жанров
    private static final String SQL_BOOK_BY_ID = """
            SELECT
                b.id,
                b.title,
                b.author_id,
                json_agg(g.id) as genre_ids
            FROM books b
            LEFT JOIN authors a on b.author_id = a.id
            LEFT JOIN books_genres bg ON b.id = bg.book_id
                INNER JOIN genres g ON bg.genre_id = g.id
            WHERE b.id = $1
            GROUP BY b.id, b.title, b.author_id, a.full_name""";

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

//    @Override
//    public Mono<BookReferenceDTO> findById(Long id) {
//        return template.getDatabaseClient().sql(SQL_BOOK_BY_ID)
//                .bind("id", id)
//                .map(this::mapToBookReferenceDto)
//                .first();
//    }

    @Override
    public Mono<BookReferenceDTO> findById(Long id) {
        return template.getDatabaseClient().inConnection(connection ->
                Mono.from(connection.createStatement(SQL_BOOK_BY_ID).bind(0, id).execute())
                        .flatMapMany(result -> result.map(this::mapToBookReferenceDto))
                        .next());
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

    private BookReferenceDTO mapToBookReferenceDto(Readable selectedRecord) {
        var bookGenresAsText = selectedRecord.get("genre_ids", String.class);
        try {
            Set<Long> bookGenres = objectMapper.readValue(bookGenresAsText, new TypeReference<>() {
            });
            return BookReferenceDTO.builder()
                    .id(selectedRecord.get("id", Long.class))
                    .title(selectedRecord.get("title", String.class))
                    .authorId(selectedRecord.get("author_id", Long.class))
                    .genreIds(bookGenres)
                    .build();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("genres:" + bookGenresAsText + " parsing error:" + e);
        }
    }
}
