package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.BaseContainerTest;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.User;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями книг")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookCommentRepositoryTest extends BaseContainerTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookCommentRepository repository;

    @DisplayName(" проверка сохранения и извлечения сущности bookComment")
    @Test
    public void testBookCommentPersistence() {
        var book = entityManager.find(Book.class, 1);
        var user = entityManager.find(User.class, 1L);

        var expectedBookComment = new BookComment(0, "comment", book, user);

        entityManager.persist(expectedBookComment);

        var actualBookComment = repository.findById(expectedBookComment.getId());

        assertThat(actualBookComment).isPresent()
                .get().usingRecursiveComparison().isEqualTo(expectedBookComment);
    }

}
