package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.service.QuestionConverter;
import ru.otus.service.QuestionConverterImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = QuestionConverterImpl.class)
@DisplayName("Класс QuestionConverterImpl")
public class QuestionConverterImplTest {

    @Autowired
    private QuestionConverterImpl questionConverter;

    @Test
    @DisplayName("правильно конвертирует вопросы в текстовое представление")
    void shouldCorrectConvertQuestionToString() {
        Question question = new Question("Name the capital of France?", List.of(
                new Answer("Paris", true),
                new Answer("Madrid", false),
                new Answer("Rome", false),
                new Answer("Berlin", false),
                new Answer("London", false)));
        String expectedQuestionText =
                "Name the capital of France?\n" +
                        "1 Paris\n" +
                        "2 Madrid\n" +
                        "3 Rome\n" +
                        "4 Berlin\n" +
                        "5 London\n";

        String actualQuestionText = questionConverter.convertQuestionToString(question);

        assertThat(actualQuestionText).isEqualTo(expectedQuestionText);
    }
}
