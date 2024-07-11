package ru.otus.hw.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.BaseContainerTest;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.services.AuthorService;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("Класс AuthorService использует CircuitBreaker")
public class AuthorServiceCircuitBreakerTest extends BaseContainerTest {

    private static final Duration WAIT_DURATION = Duration.ofSeconds(5);

    @MockBean
    private AuthorRepository authorRepository;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    private AuthorService authorService;

    @Value("${resilience4j.circuitbreaker.configs.default.sliding-window-size}")
    private int failureThreshold;

    private CircuitBreaker circuitBreaker;

    @BeforeEach
    public void setUp() {
        circuitBreakerRegistry.circuitBreaker("dbCircuitBreaker").reset();

        circuitBreaker = circuitBreakerRegistry.circuitBreaker("dbCircuitBreaker");

        when(authorRepository.findById(anyLong())).thenThrow(new RuntimeException("DB no connection"));
    }

    @Test
    @DisplayName("CircuitBreaker открывается после 5 неудачных попыток обращения к БД")
    public void testCircuitBreakerOpensAfterFailures() {

        for (int i = 0; i < failureThreshold; i++) {
            invokeFindByIdAndIgnoreExceptions();
        }

        await().atMost(WAIT_DURATION)
                .until(() -> circuitBreaker.getState() == CircuitBreaker.State.OPEN);

        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        verify(authorRepository, times(5)).findById(anyLong());
    }

    @Test
    @DisplayName("CircuitBreaker предотвращает дальнейшие вызовы БД после открытия")
    public void testCircuitBreakerPreventsFurtherCalls() {

        for (int i = 0; i < failureThreshold ; i++) {
            invokeFindByIdAndIgnoreExceptions();
        }

        await().atMost(WAIT_DURATION)
                .until(() -> circuitBreaker.getState() == CircuitBreaker.State.OPEN);

        assertThat(circuitBreaker.getState()).isEqualTo(CircuitBreaker.State.OPEN);

        invokeFindByIdAndIgnoreExceptions();

        verify(authorRepository, times(5)).findById(anyLong());
    }

    private void invokeFindByIdAndIgnoreExceptions() {
        try {
            authorService.findById(1L);
        } catch (Exception ignored) {
        }
    }

}

