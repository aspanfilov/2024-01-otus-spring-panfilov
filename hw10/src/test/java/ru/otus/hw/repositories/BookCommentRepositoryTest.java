package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями книг")
@DataJpaTest
public class BookCommentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookCommentRepository repository;

    @DisplayName(" проверка сохранения и извлечения сущности bookComment")
    @Test
    public void testBookCommentPersistence() {
        var book = entityManager.find(Book.class, 1);
        var expectedBookComment = new BookComment(0, "comment", book);

        entityManager.persist(expectedBookComment);

        var actualBookComment = repository.findById(expectedBookComment.getId());

        assertThat(actualBookComment).isPresent()
                .get().usingRecursiveComparison().isEqualTo(expectedBookComment);
    }

}
