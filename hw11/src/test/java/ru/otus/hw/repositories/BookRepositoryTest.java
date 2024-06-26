package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("Репозиторий на основе Jpa для работы с книгами")
@DataJpaTest
public class BookRepositoryTest {
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private BookRepository repository;
//
//    @DisplayName(" проверка сохранения и извлечения сущности book")
//    @Test
//    public void testBookPersistence() {
//        var expectedBook = new Book(0, "new_book",
//                new Author(0, "new_author"),
//                List.of(new Genre(0, "new_genre")));
//
//        entityManager.persist(expectedBook);
//
//        var actualBook = repository.findById(expectedBook.getId());
//
//        assertThat(actualBook).isPresent()
//                .get().usingRecursiveComparison().isEqualTo(expectedBook);
//    }

}


