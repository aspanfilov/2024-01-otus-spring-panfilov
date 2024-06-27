package ru.otus.hw;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.hw.config.ObjectMapperConfig;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.BookGenreRef;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookRepositoryCustomImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({BookRepositoryCustomImpl.class, ObjectMapperConfig.class})
public abstract class AbstractDataResetTest {

    @LocalServerPort
    private int port;

    @Autowired
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @BeforeEach
    public void resetData() {

        Mono<Void> resetData = r2dbcEntityTemplate.delete(BookComment.class).all()
                .then(r2dbcEntityTemplate.delete(BookGenreRef.class).all())
                .then(r2dbcEntityTemplate.delete(Book.class).all())
                .then(r2dbcEntityTemplate.delete(Author.class).all())
                .then(r2dbcEntityTemplate.delete(Genre.class).all())

                .then(r2dbcEntityTemplate.insert(Author.class).using(new Author(1L, "Author_1")))
                .then(r2dbcEntityTemplate.insert(Author.class).using(new Author(2L, "Author_2")))
                .then(r2dbcEntityTemplate.insert(Author.class).using(new Author(3L, "Author_3")))

                .then(r2dbcEntityTemplate.insert(Genre.class).using(new Genre(1L, "Genre_1")))
                .then(r2dbcEntityTemplate.insert(Genre.class).using(new Genre(2L, "Genre_2")))
                .then(r2dbcEntityTemplate.insert(Genre.class).using(new Genre(3L, "Genre_3")))
                .then(r2dbcEntityTemplate.insert(Genre.class).using(new Genre(4L, "Genre_4")))
                .then(r2dbcEntityTemplate.insert(Genre.class).using(new Genre(5L, "Genre_5")))
                .then(r2dbcEntityTemplate.insert(Genre.class).using(new Genre(6L, "Genre_6")))

                .then(r2dbcEntityTemplate.insert(Book.class).using(new Book(1L, "BookTitle_1", 1L)))
                .then(r2dbcEntityTemplate.insert(Book.class).using(new Book(2L, "BookTitle_2", 2L)))
                .then(r2dbcEntityTemplate.insert(Book.class).using(new Book(3L, "BookTitle_3", 3L)))

                .then(r2dbcEntityTemplate.insert(BookGenreRef.class).using(new BookGenreRef(1L, 1L)))
                .then(r2dbcEntityTemplate.insert(BookGenreRef.class).using(new BookGenreRef(1L, 2L)))
                .then(r2dbcEntityTemplate.insert(BookGenreRef.class).using(new BookGenreRef(2L, 3L)))
                .then(r2dbcEntityTemplate.insert(BookGenreRef.class).using(new BookGenreRef(2L, 4L)))
                .then(r2dbcEntityTemplate.insert(BookGenreRef.class).using(new BookGenreRef(3L, 5L)))
                .then(r2dbcEntityTemplate.insert(BookGenreRef.class).using(new BookGenreRef(3L, 6L)))

                .then(r2dbcEntityTemplate.insert(BookComment.class).using(new BookComment(1L, "comment_1", 1L)))
                .then(r2dbcEntityTemplate.insert(BookComment.class).using(new BookComment(2L, "comment_2", 1L)))
                .then(r2dbcEntityTemplate.insert(BookComment.class).using(new BookComment(3L, "comment_3", 2L)))
                .then(r2dbcEntityTemplate.insert(BookComment.class).using(new BookComment(4L, "comment_4", 2L)))
                .then(r2dbcEntityTemplate.insert(BookComment.class).using(new BookComment(5L, "comment_5", 3L)))
                .then(r2dbcEntityTemplate.insert(BookComment.class).using(new BookComment(6L, "comment_6", 3L)))
                .then(r2dbcEntityTemplate.insert(BookComment.class).using(new BookComment(7L, "comment_7", 3L)))
                .then(r2dbcEntityTemplate.insert(BookComment.class).using(new BookComment(8L, "comment_8", 3L)))

                .then(r2dbcEntityTemplate.getDatabaseClient().sql("ALTER SEQUENCE authors_id_seq RESTART WITH 4").fetch().rowsUpdated())
                .then(r2dbcEntityTemplate.getDatabaseClient().sql("ALTER SEQUENCE genres_id_seq RESTART WITH 7").fetch().rowsUpdated())
                .then(r2dbcEntityTemplate.getDatabaseClient().sql("ALTER SEQUENCE books_id_seq RESTART WITH 4").fetch().rowsUpdated())
                .then(r2dbcEntityTemplate.getDatabaseClient().sql("ALTER SEQUENCE book_comments_id_seq RESTART WITH 9").fetch().rowsUpdated())

                .then();

        resetData.subscribeOn(Schedulers.boundedElastic()).block();
    }

}
