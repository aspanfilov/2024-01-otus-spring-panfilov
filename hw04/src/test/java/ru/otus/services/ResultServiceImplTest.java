package ru.otus.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.config.TestConfig;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.Student;
import ru.otus.domain.TestResult;
import ru.otus.service.IO.IOService;
import ru.otus.service.IO.LocalizedIOService;
import ru.otus.service.ResultServiceImpl;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Класс ResultServiceImpl")
@SpringBootTest(classes = ResultServiceImpl.class)
public class ResultServiceImplTest {

    @MockBean
    private TestConfig testConfig;

    @MockBean
    private LocalizedIOService ioService;

    @Autowired
    private ResultServiceImpl resultService;

    private TestResult testResult;

    @BeforeEach
    void setUp() {
        Student student = new Student("Ivan", "Ivanov");
        Answer answer = new Answer("Answer", true);
        Question question = new Question("Question", List.of(answer));
        testResult = new TestResult(student);
        testResult.applyAnswer(question, true);
    }

    @Test
    @DisplayName("Поздравляет студента, если тест пройден")
    void shouldCongratulateStudentIfPassed() {
        when(testConfig.getRightAnswersCountToPass()).thenReturn(1);

        resultService.showResult(testResult);

        verify(ioService).printLineLocalized("ResultService.passed.test");
    }

    @Test
    @DisplayName("Оповещает студента, если тест провален")
    void shouldInformStudentOfFailureIfNotPassed() {
        when(testConfig.getRightAnswersCountToPass()).thenReturn(2);

        resultService.showResult(testResult);

        verify(ioService).printLineLocalized("ResultService.fail.test");
    }
}
