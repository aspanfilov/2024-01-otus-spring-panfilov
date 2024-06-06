package ru.otus.hw.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.BookComment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookCommentRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class InitMongoDBDataChangeLog {

    private Author author1 = new Author(null, "Author_1");

    private Author author2 = new Author(null, "Author_2");

    private Author author3 = new Author(null, "Author_3");

    private Genre genre1 = new Genre(null, "Genre_1");

    private Genre genre2 = new Genre(null, "Genre_2");

    private Genre genre3 = new Genre(null, "Genre_3");

    private Genre genre4 = new Genre(null, "Genre_4");

    private Genre genre5 = new Genre(null, "Genre_5");

    private Genre genre6 = new Genre(null, "Genre_6");

    private Book book1 = new Book(null, "BookTitle_1", author1, List.of(genre1, genre2));

    private Book book2 = new Book(null, "BookTitle_2", author2, List.of(genre3, genre4));

    private Book book3 = new Book(null, "BookTitle_3", author3, List.of(genre5, genre6));

    private BookComment comment1 = new BookComment(null, "comment_1", book1);

    private BookComment comment2 = new BookComment(null, "comment_2", book1);

    private BookComment comment3 = new BookComment(null, "comment_3", book2);

    private BookComment comment4 = new BookComment(null, "comment_4", book2);

    private BookComment comment5 = new BookComment(null, "comment_5", book3);

    private BookComment comment6 = new BookComment(null, "comment_6", book3);


    @ChangeSet(order = "001", id = "dropDb", author = "Panfilov Artur", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insertAuthors", author = "Panfilov Artur", runAlways = true)
    public void insertAuthors(AuthorRepository authorRepository) {
        authorRepository.saveAll(List.of(author1, author2, author3));
    }

    @ChangeSet(order = "003", id = "insertGenres", author = "Panfilov Artur", runAlways = true)
    public void insertGenres(GenreRepository genreRepository) {
        genreRepository.saveAll(List.of(genre1, genre2, genre3, genre4, genre5, genre6));
    }

    @ChangeSet(order = "004", id = "insertBooks", author = "Panfilov Artur", runAlways = true)
    public void insertBooks(BookRepository bookRepository) {
        bookRepository.saveAll(List.of(book1, book2, book3));
    }

    @ChangeSet(order = "005", id = "insertBookComments", author = "Panfilov Artur", runAlways = true)
    public void insertBookComments(BookCommentRepository bookCommentRepository) {
        bookCommentRepository.saveAll(List.of(comment1, comment2, comment3, comment4, comment5, comment6));
    }

}
