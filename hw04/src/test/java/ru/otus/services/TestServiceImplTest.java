package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dao.QuestionDao;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.Student;
import ru.otus.domain.TestResult;
import ru.otus.service.IO.LocalizedIOService;
import ru.otus.service.QuestionConverter;
import ru.otus.service.TestServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Класс TestServiceImpl")
@SpringBootTest(classes = TestServiceImpl.class)
public class TestServiceImplTest {

    @MockBean
    private LocalizedIOService ioService;

    @MockBean
    private QuestionDao questionDao;

    @MockBean
    private QuestionConverter questionConverter;

    @Autowired
    private TestServiceImpl testService;

    @Test
    @DisplayName("Должен корректно выполнить тестирование")
    void shouldCorrectExecuteTest() {
        Student anyStudent = new Student("Ivan", "Ivanov");
        Question question = new Question("Name the capital of France?", List.of(
                new Answer("Paris", true),
                new Answer("Madrid", false),
                new Answer("Rome", false),
                new Answer("Berlin", false),
                new Answer("London", false)));

        when(questionDao.findAll())
                .thenReturn(List.of(question));
        when(questionConverter.convertQuestionToString(question))
                .thenReturn("formatted question text");
        when(ioService.readIntForRangeWithPromptLocalized(eq(1), eq(question.answers().size()),
                eq("TestService.input.correct.answer"), any()))
                .thenReturn(1);

        TestResult testResult = testService.executeTestFor(anyStudent);

        verify(ioService).printLine("formatted question text");
        verify(ioService).readIntForRangeWithPromptLocalized(eq(1), eq(question.answers().size()),
                eq("TestService.input.correct.answer"), any());
        assertThat(testResult.getRightAnswersCount()).isEqualTo(1);
    }
}
