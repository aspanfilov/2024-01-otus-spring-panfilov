package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.dao.QuestionDao;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.Student;
import ru.otus.domain.TestResult;
import ru.otus.service.IO.IOService;
import ru.otus.service.QuestionConverter;
import ru.otus.service.TestServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Класс TestServiceImpl")
@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private QuestionConverter questionConverter;

    @Mock
    private Student anyStudent;

    @InjectMocks
    private TestServiceImpl testService;

    @Test
    @DisplayName("Должен корректно выполнить тестирование")
    void shouldCorrectExecuteTest() {
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
        when(ioService.readIntForRangeWithPrompt(eq(1), eq(question.answers().size()),
                eq("Please input correct answer number"), any()))
                .thenReturn(1);

        TestResult testResult = testService.executeTestFor(anyStudent);

        verify(ioService).printLine("formatted question text");
        verify(ioService).readIntForRangeWithPrompt(eq(1), eq(question.answers().size()),
                eq("Please input correct answer number"), any());
        assertThat(testResult.getRightAnswersCount()).isEqualTo(1);
    }
}
