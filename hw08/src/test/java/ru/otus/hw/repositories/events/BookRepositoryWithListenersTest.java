package ru.otus.hw.repositories.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.events.MongoBookCascadeDeleteListener;
import ru.otus.hw.events.MongoBookCascadeSaveEventsListener;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@DisplayName("класс BookRepository")
@Import({MongoBookCascadeSaveEventsListener.class, MongoBookCascadeDeleteListener.class})
public class BookRepositoryWithListenersTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookCommentRepository bookCommentRepository;

    private Book book;

    @BeforeEach
    void setUp() {
        Author author = Author.builder()
                .fullName("new_author").build();
        Genre genre1 = Genre.builder()
                .name("new_genre1").build();
        Genre genre2 = Genre.builder()
                .name("new_genre2").build();
        book = Book.builder()
                .title("new_book")
                .author(author)
                .genres(List.of(genre1, genre2))
                .build();
    }

    @DisplayName("При сохранении книги сохраняет автора и жанры")
    @Test
    void shouldSaveAuthorAndGenresWhenSaveBook() {
        var actualBook = bookRepository.save(book);

        assertThat(actualBook.getId()).isNotNull();
        assertThat(actualBook.getAuthor().getId()).isNotNull();
        assertThat(actualBook.getGenres().get(0).getId()).isNotNull();
        assertThat(actualBook.getGenres().get(1).getId()).isNotNull();
    }

    @DisplayName("Должен удалить комментарии при удалении книги")
    @Test
    void shouldDeleteCommentsWhenDeleteBook() {
        var actualBook = bookRepository.save(book);
        var comment = BookComment.builder()
                .commentText("new_comment")
                .book(actualBook)
                .build();
        var actualComment = bookCommentRepository.save(comment);

        bookRepository.deleteById(book.getId());

        assertThat(bookCommentRepository.findById(actualComment.getId())).isEmpty();
    }


}
