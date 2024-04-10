package ru.otus.hw.repositories;

import lombok.val;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями к книгам")
@DataJpaTest
@Import({JpaBookCommentRepository.class})
public class JpaBookCommentRepositoryTest {

    private static final int EXPECTED_QUERIES_COUNT = 2;
    private static final int FIRST_BOOK_ID = 1;

    @Autowired
    private JpaBookCommentRepository repositoryJpa;

    @Autowired
    private TestEntityManager entityManager;

    private List<Author> dbAuthors;

    private List<Genre> dbGenres;

    private List<Book> dbBooks;

    private List<BookComment> dbBookComments;

    @BeforeEach
    void setUp() {
        dbAuthors = getDbAuthors();
        dbGenres = getDbGenres();
        dbBooks = getDbBooks(dbAuthors, dbGenres);
        dbBookComments = getDbBookComment(dbBooks);
    }

    @DisplayName("должен загружать книжный комментарий по id")
    @ParameterizedTest
    @MethodSource("getDbBookComment")
    void shouldReturnCorrectBookById(BookComment expectedBookComment) {
        val actualBookComment = repositoryJpa.findById(expectedBookComment.getId());

        val unproxiedBook = (Book) Hibernate.unproxy(actualBookComment.get().getBook());
        actualBookComment.get().setBook(unproxiedBook);

        assertThat(actualBookComment).isPresent()
                .get().usingRecursiveComparison().isEqualTo(expectedBookComment);
    }

    @DisplayName("должен загружать список всех комментариев книг")
    @Test
    void shouldReturnCorrectAllBookComments() {
        val actualBookComments = repositoryJpa.findAll();
        val expectedBookComments = dbBookComments;

        assertThat(actualBookComments).hasSize(expectedBookComments.size());
        assertThat(actualBookComments).usingRecursiveComparison().isEqualTo(expectedBookComments);
    }

    @DisplayName("должен загружать список всех комментариев книг с полной информацией за указанное количество запросов")
    @Test
    void shouldLoadAllBookCommentsWithFullInfoInGivenQueryCount() {
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();

        val bookComments = repositoryJpa.findAll();
        assertThat(bookComments).isNotNull().hasSize(6)
                .allMatch(bc -> !bc.getCommentText().equals(""))
                .allMatch(bc -> !bc.getBook().getTitle().equals(""))
                .allMatch(bc -> !bc.getBook().getAuthor().getFullName().equals(""))
                .allMatch(bc -> bc.getBook().getGenres().size() > 0);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @DisplayName("должен загружать список всех комментариев по id книги")
    @Test
    void shouldReturnCorrectAllCommentsByBookId() {
        val actualBookComments = repositoryJpa.findAllByBookId(FIRST_BOOK_ID);
        val expectedBookComments = dbBookComments.stream()
                .filter(bc -> bc.getBook().getId() == FIRST_BOOK_ID).toList();

        assertThat(actualBookComments).hasSize(expectedBookComments.size());
        assertThat(actualBookComments).usingRecursiveComparison().isEqualTo(expectedBookComments);
    }

    @DisplayName("должен загружать список всех комментариев по id книги с полной информацией за указанное количество запросов")
    @Test
    void shouldLoadAllCommentsByBookIdWithFullInfoInGivenQueryCount() {
        SessionFactory sessionFactory = entityManager.getEntityManager().getEntityManagerFactory()
                .unwrap(SessionFactory.class);
        sessionFactory.getStatistics().setStatisticsEnabled(true);
        sessionFactory.getStatistics().clear();

        val bookComments = repositoryJpa.findAllByBookId(1);
        assertThat(bookComments).isNotNull().hasSize(2)
                .allMatch(bc -> !bc.getCommentText().equals(""))
                .allMatch(bc -> !bc.getBook().getTitle().equals(""))
                .allMatch(bc -> !bc.getBook().getAuthor().getFullName().equals(""))
                .allMatch(bc -> bc.getBook().getGenres().size() > 0);

        assertThat(sessionFactory.getStatistics().getPrepareStatementCount()).isEqualTo(EXPECTED_QUERIES_COUNT);
    }

    @DisplayName("должен корректно сохранять новый комментарий книги")
    @Test
    void shouldSaveNewBookComment() {
        var expectedBookComment = new BookComment(0, "new_comment",
                entityManager.find(Book.class, dbBooks.get(1).getId()));

        repositoryJpa.save(expectedBookComment);
        assertThat(expectedBookComment.getId()).isGreaterThan(0);

        val actualBookComment = entityManager.find(BookComment.class, expectedBookComment.getId());

        assertThat(actualBookComment)
                .usingRecursiveComparison().isEqualTo(expectedBookComment);
    }

    @DisplayName("должен сохранять измененный комментарий книги")
    @Test
    void shouldSaveUpdatedBookComment() {
        var expectedBookComment = new BookComment(1, "updatable_comment",
                entityManager.find(Book.class, dbBooks.get(1).getId()));

        assertThat(entityManager.find(BookComment.class, expectedBookComment.getId()))
                .isNotEqualTo(expectedBookComment);

        var returnedBookComment = repositoryJpa.save(expectedBookComment);
        assertThat(returnedBookComment).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().isEqualTo(expectedBookComment);

        assertThat(entityManager.find(BookComment.class, returnedBookComment.getId()))
                .usingRecursiveComparison().isEqualTo(returnedBookComment);
    }

    @DisplayName("должен удалять комментарий книги по id ")
    @Test
    void shouldDeleteBookComment() {
        assertThat(entityManager.find(BookComment.class, 1)).isNotNull();
        repositoryJpa.deleteById(1);
        assertThat(entityManager.find(BookComment.class, 1)).isNull();
    }

    private static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Author(id, "Author_" + id))
                .toList();
    }

    private static List<Genre> getDbGenres() {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Genre(id, "Genre_" + id))
                .toList();
    }

    private static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
                .map(id -> new Book(id,
                        "BookTitle_" + id,
                        dbAuthors.get(id - 1),
                        dbGenres.subList((id - 1) * 2, (id - 1) * 2 + 2)
                ))
                .toList();
    }

    private static List<BookComment> getDbBookComment(List<Book> dbBooks) {
        return IntStream.rangeClosed(1, 6).boxed()
                .map(id -> new BookComment(id,
                        "comment_" + id,
                        dbBooks.get((id - 1) / 2)
                ))
                .toList();
    }

    private static List<BookComment> getDbBookComment() {
        var dbAuthors = getDbAuthors();
        var dbGenres = getDbGenres();
        var dbBooks = getDbBooks(dbAuthors, dbGenres);
        return getDbBookComment(dbBooks);
    }
}