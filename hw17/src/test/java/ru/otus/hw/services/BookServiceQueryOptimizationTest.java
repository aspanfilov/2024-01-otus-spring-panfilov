package ru.otus.hw.services;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.BaseContainerTest;
import ru.otus.hw.mappers.BookMapper;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование оптимизации запросов в BookService")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({BookServiceImpl.class, BookMapper.class})
public class BookServiceQueryOptimizationTest extends BaseContainerTest {

    private static final long FIRST_BOOK_ID = 1;
    private static final int BOOKS_COUNT = 3;
    private static final int FINDBYID_EXPECTED_QUERIES_COUNT = 1;
    private static final int FINDALL_EXPECTED_QUERIES_COUNT = 2;

    @Autowired
    private BookService bookService;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        sessionFactory.getStatistics().clear();
        sessionFactory.getStatistics().setStatisticsEnabled(true);
    }

    @DisplayName("при выполнении findAll - выполняется 2 запроса в БД")
    @Test
    public void whenFindAllIsCalled_thenExecuteOnlyTwoQueries() {
        var bookDTOs = bookService.findAll();
        long executedQueries = sessionFactory.getStatistics().getPrepareStatementCount();

        assertThat(bookDTOs).hasSize(BOOKS_COUNT);
        assertThat(executedQueries).isEqualTo(FINDALL_EXPECTED_QUERIES_COUNT);
    }

    @DisplayName("при выполнении findById - выполняется 1 запрос в БД")
    @Test
    public void whenFindByIdIsCalled_thenExecuteOnlyOneQuery() {
        var bookDTO = bookService.findById(FIRST_BOOK_ID);
        long executedQueries = sessionFactory.getStatistics().getPrepareStatementCount();

        assertThat(bookDTO).isPresent();
        assertThat(executedQueries).isEqualTo(FINDBYID_EXPECTED_QUERIES_COUNT);
    }

}
