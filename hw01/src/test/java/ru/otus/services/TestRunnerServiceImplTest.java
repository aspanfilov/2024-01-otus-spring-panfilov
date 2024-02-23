package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.service.TestRunnerServiceImpl;
import ru.otus.service.TestService;

import static org.mockito.Mockito.verify;

@DisplayName("Сервис по проведению тестирования")
@ExtendWith(MockitoExtension.class)
public class TestRunnerServiceImplTest {

    @Mock
    private TestService testService;

    @InjectMocks
    private TestRunnerServiceImpl testRunnerService;

    @DisplayName("Метод run должен вызывать executeTest на TestService")
    @Test
    void shouldCallMethodExecuteTest() {
        testRunnerService.run();
        verify(testService).executeTest();
    }
}
