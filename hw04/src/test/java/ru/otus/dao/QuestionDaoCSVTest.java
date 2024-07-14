package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.config.TestFileNameProvider;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Класс QuestionDaoCSV")
@SpringBootTest(classes = QuestionDaoCSV.class)
public class QuestionDaoCSVTest {

    @MockBean
    private TestFileNameProvider testFileNameProvider;

    @Autowired
    private QuestionDaoCSV questionDaoCSV;

    @Test
    @DisplayName("findAll должен корректно считывать вопросы из CSV файла")
    void findAllShouldCorrectlyReadQuestionsFromCsvFile() {
        when(testFileNameProvider.getTestFileName()).thenReturn("questions.csv");

        List<Question> questions = questionDaoCSV.findAll();

        assertThat(questions)
                .hasSize(5)
                .containsExactly(new Question("Name the capital of France?", List.of(
                                new Answer("Paris", true),
                                new Answer("Madrid", false),
                                new Answer("Rome", false),
                                new Answer("Berlin", false),
                                new Answer("London", false))),
                        new Question("Name the capital of Japan?", List.of(
                                new Answer("Seoul", false),
                                new Answer("Beijing", false),
                                new Answer("Kyoto", false),
                                new Answer("Bangkok", false),
                                new Answer("Tokyo", true))),
                        new Question("Name the capital of Australia?", List.of(
                                new Answer("Sydney", false),
                                new Answer("Melbourne", false),
                                new Answer("Canberra", true),
                                new Answer("Brisbane", false),
                                new Answer("Adelaide", false))),
                        new Question("Name the capital of Canada?", List.of(
                                new Answer("Toronto", false),
                                new Answer("Vancouver", false),
                                new Answer("Ottawa", true),
                                new Answer("Montreal", false),
                                new Answer("Quebec", false))),
                        new Question("Name the capital of Brazil?", List.of(
                                new Answer("Rio de Janeiro", false),
                                new Answer("Sao Paulo", false),
                                new Answer("Brasilia", true),
                                new Answer("Salvador", false),
                                new Answer("Belo Horizonte", false))));
    }

}
