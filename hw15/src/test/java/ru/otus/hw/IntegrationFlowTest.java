package ru.otus.hw;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.divine.domain.Destination;
import ru.otus.hw.divine.domain.Soul;
import ru.otus.hw.domain.Intention;
import ru.otus.hw.services.LifeCycleGeneratorRunner;
import ru.otus.hw.services.LifeCyclesService;
import ru.otus.hw.services.LifeGateway;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Интеграционный тест жизненного цикла")
public class IntegrationFlowTest {

    @MockBean
    private LifeCyclesService lifeCyclesService;

    @MockBean
    private LifeCycleGeneratorRunner lifeCycleGeneratorRunner;

    @Autowired
    private LifeGateway lifeGateway;

    @Test
    @DisplayName("Прогон 1 жизни по всей цепи")
    public void testLifeFlow() {

        Soul soul = lifeGateway.execute(new Intention());

        assertThat(soul).isNotNull();
        assertThat(soul.getUsedPerson()).isNotNull();
        assertThat(soul.getUsedPerson().getName()).isNotNull().isNotBlank();
        assertThat(soul.getDestination()).isNotNull();

        if (soul.getKarma() >= 0) {
            assertThat(soul.getDestination()).isEqualTo(Destination.HEAVEN);
        } else {
            assertThat(soul.getDestination()).isEqualTo(Destination.HELL);
        }

    }
}
