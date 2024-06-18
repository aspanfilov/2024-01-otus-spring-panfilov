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
import ru.otus.hw.mappers.BookCommentMapper;
import ru.otus.hw.repositories.BookCommentRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование оптимизации запросов в BookCommentService")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({BookCommentServiceImpl.class, BookCommentMapper.class, BookServiceImpl.class, UserServiceImpl.class})
public class BookCommentServiceQueryOptimizationTest extends BaseContainerTest {

    private static final long FIRST_BOOK_ID = 1;
    private static final long FIRST_BOOKCOMMENT_ID = 1;
    private static final int FIRST_BOOK_COMMENTS_COUNT = 2;
    private static final int FINDBYID_EXPECTED_QUERIES_COUNT = 1;
    private static final int FINDALLBYBOOKID_EXPECTED_QUERIES_COUNT = 2;

    @Autowired
    private BookCommentService bookCommentService;

    @Autowired
    private SessionFactory sessionFactory;

    @BeforeEach
    public void setUp() {
        sessionFactory.getStatistics().clear();
        sessionFactory.getStatistics().setStatisticsEnabled(true);
    }

    @DisplayName("при выполнении findAllByBookId - выполняется 2 запроса в БД")
    @Test
    public void whenFindAllByBookIdIsCalled_thenExecuteOnlyTwoQueries() {
        var bookCommentDTOs = bookCommentService.findAllByBookId(FIRST_BOOK_ID);
        long executedQueries = sessionFactory.getStatistics().getPrepareStatementCount();

        assertThat(bookCommentDTOs).hasSize(FIRST_BOOK_COMMENTS_COUNT);
        assertThat(executedQueries).isEqualTo(FINDALLBYBOOKID_EXPECTED_QUERIES_COUNT);
    }

    @DisplayName("при выполнении findById - выполняется 1 запрос в БД")
    @Test
    public void whenFindByIdIsCalled_thenExecuteOnlyOneQuery() {
        var bookCommentDTO = bookCommentService.findById(FIRST_BOOKCOMMENT_ID);
        long executedQueries = sessionFactory.getStatistics().getPrepareStatementCount();

        assertThat(bookCommentDTO).isPresent();
        assertThat(executedQueries).isEqualTo(FINDBYID_EXPECTED_QUERIES_COUNT);
    }

}
