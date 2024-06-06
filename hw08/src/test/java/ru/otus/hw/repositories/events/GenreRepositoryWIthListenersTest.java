package ru.otus.hw.repositories.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.events.MongoGenreCascadeDeleteListener;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@DisplayName("класс GenreRepository")
@Import(MongoGenreCascadeDeleteListener.class)
public class GenreRepositoryWIthListenersTest {

//    @Autowired
//    private AuthorRepository authorRepository;
//
//    @Autowired
//    private GenreRepository genreRepository;
//
//    @Autowired
//    private BookRepository bookRepository;
//
//    @DisplayName("При удалении жанра если с ним есть книга то должно быть брошено исключене")
//    @Test
//    void shouldThrowExceptionWhenDeleteGenreWithBook() {
//
//        Author author = Author.builder()
//                .fullName("new_author").build();
//        authorRepository.save(author);
//
//        Genre genre = Genre.builder()
//                .name("new_genre").build();
//        genreRepository.save(genre);
//
//        Book book = Book.builder()
//                .title("new_book")
//                .author(author)
//                .genres(List.of(genre))
//                .build();
//        bookRepository.save(book);
//
//        assertThatThrownBy(() -> genreRepository.deleteById(genre.getId()))
//                .isInstanceOf(IllegalStateException.class)
//                .hasMessageContaining("Genre cannot be deleted as it is referenced by a book.");
//    }

}
