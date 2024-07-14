package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.domain.Student;
import ru.otus.domain.TestResult;
import ru.otus.registration.StudentRegistrationManager;
import ru.otus.service.ResultService;
import ru.otus.service.TestRunnerImpl;
import ru.otus.service.TestService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Сервис по проведению тестирования")
@SpringBootTest(classes = TestRunnerImpl.class)
public class TestRunnerServiceImplTest {

    @MockBean
    private TestService testService;

    @MockBean
    private StudentRegistrationManager studentRegistrationManager;

    @MockBean
    private ResultService resultService;

    @Autowired
    private TestRunnerImpl testRunnerService;

    @DisplayName("Метод run должен корректно исполнять процесс тестирования")
    @Test
    void runShouldCorrectlyExecuteTestingProcess() {
        Student expectedStudent = new Student("Ivan", "Ivanov");
        when(studentRegistrationManager.getRegisteredStudent())
                .thenReturn(expectedStudent);
        TestResult expectedTestResult = new TestResult(expectedStudent);
        when(testService.executeTestFor(expectedStudent))
                .thenReturn(expectedTestResult);

        testRunnerService.run();
        verify(studentRegistrationManager).getRegisteredStudent();
        verify(testService).executeTestFor(expectedStudent);
        verify(resultService).showResult(expectedTestResult);
    }
}
