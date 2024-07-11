package ru.otus.hw;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class BaseContainerTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("postgres");

    static {
        POSTGRE_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRE_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRE_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRE_SQL_CONTAINER::getPassword);
    }

    @BeforeEach
    public void resetData() {
        jdbcTemplate.execute("DELETE FROM book_comments");
        jdbcTemplate.execute("DELETE FROM books_genres");
        jdbcTemplate.execute("DELETE FROM books");
        jdbcTemplate.execute("DELETE FROM genres");
        jdbcTemplate.execute("DELETE FROM authors");

        //так как данные будут перезаполняться многократно, вручную вставляем идентфикаторы
        jdbcTemplate.execute("INSERT INTO authors(id, full_name) VALUES (1, 'Author_1'), (2, 'Author_2'), (3, 'Author_3')");
        jdbcTemplate.execute("INSERT INTO genres(id, name) VALUES (1, 'Genre_1'), (2, 'Genre_2'), (3, 'Genre_3'), (4, 'Genre_4'), (5, 'Genre_5'), (6, 'Genre_6')");
        jdbcTemplate.execute("INSERT INTO books(id, title, author_id) VALUES (1, 'BookTitle_1', 1), (2, 'BookTitle_2', 2), (3, 'BookTitle_3', 3)");
        jdbcTemplate.execute("INSERT INTO books_genres(book_id, genre_id) VALUES (1, 1), (1, 2), (2, 3), (2, 4), (3, 5), (3, 6)");
        jdbcTemplate.execute("INSERT INTO book_comments(id, comment_text, book_id, user_id) VALUES (1, 'comment_1', 1, 2), (2, 'comment_2', 1, 3), (3, 'comment_3', 2, 2), (4, 'comment_4', 2, 2), (5, 'comment_5', 3, 2), (6, 'comment_6', 3, 2), (7, 'comment_7', 3, 2), (8, 'comment_8', 3, 2)");

        //так как вручную задавали идентификаторы, то предустанавливаем счетчики
        jdbcTemplate.execute("ALTER SEQUENCE authors_id_seq RESTART WITH 4");
        jdbcTemplate.execute("ALTER SEQUENCE genres_id_seq RESTART WITH 7");
        jdbcTemplate.execute("ALTER SEQUENCE books_id_seq RESTART WITH 4");
        jdbcTemplate.execute("ALTER SEQUENCE book_comments_id_seq RESTART WITH 9");
    }
}
