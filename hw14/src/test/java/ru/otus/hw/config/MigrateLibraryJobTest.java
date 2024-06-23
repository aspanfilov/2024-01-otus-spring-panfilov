package ru.otus.hw.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoBookComment;
import ru.otus.hw.models.mongo.MongoGenre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.config.JobConfig.LIBRARY_MIGRATION_JOB_NAME;

@SpringBootTest
@SpringBatchTest
@DisplayName("Тестирование батча миграции библиотеки")
public class MigrateLibraryJobTest {

    private static final int AUTHORS_COUNT = 3;
    private static final int GENRES_COUNT = 6;
    private static final int BOOKS_COUNT = 3;
    private static final int BOOK_COMMENTS_COUNT = 6;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
        mongoTemplate.dropCollection(MongoAuthor.class);
        mongoTemplate.dropCollection(MongoGenre.class);
        mongoTemplate.dropCollection(MongoBook.class);
        mongoTemplate.dropCollection(MongoBookComment.class);
    }

    @Test
    @DisplayName("проверка корректного переноса данных в MongoDB")
    void testJob() throws Exception {

        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(LIBRARY_MIGRATION_JOB_NAME);

        JobParameters parameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        verifyAuthors();
        verifyGenres();
        verifyBooks();
        verifyBookComments();

    }

    private void verifyAuthors() {
        List<MongoAuthor> mongoAuthors = mongoTemplate.findAll(MongoAuthor.class);
        assertThat(mongoAuthors)
                .hasSize(AUTHORS_COUNT)
                .extracting(MongoAuthor::getFullName)
                .containsExactlyInAnyOrder("Author_1", "Author_2", "Author_3");
    }

    private void verifyGenres() {
        List<MongoGenre> mongoGenres = mongoTemplate.findAll(MongoGenre.class);
        assertThat(mongoGenres)
                .hasSize(GENRES_COUNT)
                .extracting(MongoGenre::getName)
                .containsExactlyInAnyOrder("Genre_1", "Genre_2", "Genre_3", "Genre_4", "Genre_5", "Genre_6");
    }

    private void verifyBooks() {
        List<MongoBook> mongoBooks = mongoTemplate.findAll(MongoBook.class);
        assertThat(mongoBooks)
                .hasSize(BOOKS_COUNT);

        MongoBook book1 = mongoBooks.stream()
                .filter(book -> book.getTitle().equals("BookTitle_1"))
                .findFirst().orElse(null);
        assertThat(book1).isNotNull();
        assertThat(book1.getAuthor().getFullName()).isEqualTo("Author_1");
        assertThat(book1.getGenres())
                .extracting(MongoGenre::getName)
                .containsExactlyInAnyOrder("Genre_1", "Genre_2");

        MongoBook book2 = mongoBooks.stream()
                .filter(book -> book.getTitle().equals("BookTitle_2"))
                .findFirst().orElse(null);
        assertThat(book2).isNotNull();
        assertThat(book2.getAuthor().getFullName()).isEqualTo("Author_2");
        assertThat(book2.getGenres())
                .extracting(MongoGenre::getName)
                .containsExactlyInAnyOrder("Genre_3", "Genre_4");

        MongoBook book3 = mongoBooks.stream()
                .filter(book -> book.getTitle().equals("BookTitle_3"))
                .findFirst().orElse(null);
        assertThat(book3).isNotNull();
        assertThat(book3.getAuthor().getFullName()).isEqualTo("Author_3");
        assertThat(book3.getGenres())
                .extracting(MongoGenre::getName)
                .containsExactlyInAnyOrder("Genre_5", "Genre_6");
    }

    private void verifyBookComments() {
        List<MongoBookComment> mongoBookComments = mongoTemplate.findAll(MongoBookComment.class);
        assertThat(mongoBookComments)
                .hasSize(BOOK_COMMENTS_COUNT);

        MongoBookComment comment1 = mongoBookComments.stream()
                .filter(comment -> comment.getCommentText().equals("Comment_1"))
                .findFirst().orElse(null);
        assertThat(comment1).isNotNull();
        assertThat(comment1.getBook().getTitle()).isEqualTo("BookTitle_1");

        MongoBookComment comment2 = mongoBookComments.stream()
                .filter(comment -> comment.getCommentText().equals("Comment_2"))
                .findFirst().orElse(null);
        assertThat(comment2).isNotNull();
        assertThat(comment2.getBook().getTitle()).isEqualTo("BookTitle_2");

        MongoBookComment comment3 = mongoBookComments.stream()
                .filter(comment -> comment.getCommentText().equals("Comment_3"))
                .findFirst().orElse(null);
        assertThat(comment3).isNotNull();
        assertThat(comment3.getBook().getTitle()).isEqualTo("BookTitle_3");

        MongoBookComment comment4 = mongoBookComments.stream()
                .filter(comment -> comment.getCommentText().equals("Comment_4"))
                .findFirst().orElse(null);
        assertThat(comment4).isNotNull();
        assertThat(comment4.getBook().getTitle()).isEqualTo("BookTitle_3");

        MongoBookComment comment5 = mongoBookComments.stream()
                .filter(comment -> comment.getCommentText().equals("Comment_5"))
                .findFirst().orElse(null);
        assertThat(comment5).isNotNull();
        assertThat(comment5.getBook().getTitle()).isEqualTo("BookTitle_3");

        MongoBookComment comment6 = mongoBookComments.stream()
                .filter(comment -> comment.getCommentText().equals("Comment_6"))
                .findFirst().orElse(null);
        assertThat(comment6).isNotNull();
        assertThat(comment6.getBook().getTitle()).isEqualTo("BookTitle_3");
    }

}
