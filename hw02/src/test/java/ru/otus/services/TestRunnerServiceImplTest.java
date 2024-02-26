package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.domain.Student;
import ru.otus.domain.TestResult;
import ru.otus.service.ResultService;
import ru.otus.service.StudentService;
import ru.otus.service.TestRunnerServiceImpl;
import ru.otus.service.TestService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Сервис по проведению тестирования")
@ExtendWith(MockitoExtension.class)
public class TestRunnerServiceImplTest {

    @Mock
    private TestService testService;

    @Mock
    private StudentService studentService;

    @Mock
    private ResultService resultService;

    @InjectMocks
    private TestRunnerServiceImpl testRunnerService;

    @DisplayName("Метод run должен корректно исполнять процесс тестирования")
    @Test
    void runShouldCorrectlyExecuteTestingProcess() {
        Student expectedStudent = new Student("Ivan", "Ivanov");
        when(studentService.determineCurrentStudent())
                .thenReturn(expectedStudent);
        TestResult expectedTestResult = new TestResult(expectedStudent);
        when(testService.executeTestFor(expectedStudent))
                .thenReturn(expectedTestResult);

        testRunnerService.run();
        verify(studentService).determineCurrentStudent();
        verify(testService).executeTestFor(expectedStudent);
        verify(resultService).showResult(expectedTestResult);
    }
}
