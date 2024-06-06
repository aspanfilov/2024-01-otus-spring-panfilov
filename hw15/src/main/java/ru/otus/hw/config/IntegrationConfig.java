package ru.otus.hw.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import ru.otus.hw.divine.domain.Destination;
import ru.otus.hw.divine.domain.Soul;
import ru.otus.hw.mappers.KarmaConverter;
import ru.otus.hw.mortal.services.LifeService;

@Configuration
@EnableConfigurationProperties(AppProps.class)
public class IntegrationConfig {

    @Bean
    public MessageChannelSpec<?, ?> lifeChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> lifeReportChannel() {
        return MessageChannels.direct();
    }

    @Bean
    public MessageChannelSpec<?, ?> heavenChannel() {
        return MessageChannels.direct();
    }

    @Bean
    public MessageChannelSpec<?, ?> hellChannel() {
        return MessageChannels.direct();
    }

    @ConditionalOnProperty(name = "app.parallel-processing", havingValue = "false")
    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec sequentialPoller() {
        return Pollers.fixedRate(100).maxMessagesPerPoll(1);
    }

    @ConditionalOnProperty(name = "app.parallel-processing", havingValue = "true")
    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec parallelPoller(TaskExecutor taskExecutor) {
        return Pollers.fixedRate(100).maxMessagesPerPoll(2).taskExecutor(taskExecutor);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("LifeTaskExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public IntegrationFlow lifeFlow(LifeService lifeService) {

        return IntegrationFlow.from(lifeChannel())
                //Пример обращения к сервису через внедренный бин
                .handle(lifeService, "liveLife")
                .split("payload.deeds")
                .transform(KarmaConverter::deedToKarmaPoint)
                .aggregate()
                //Пример обращения к сервису через строковое имя
                .handle("judgementService", "judgePersonKarmaBaggage")
                .<Soul, Destination>route(Soul::getDestination, mapping -> mapping
                        .subFlowMapping(Destination.HEAVEN, sf -> sf
                                .channel(heavenChannel())
                                .channel(lifeReportChannel()))
                        .subFlowMapping(Destination.HELL, sf -> sf
                                .channel(hellChannel())
                                .channel(lifeReportChannel())))
                .get();
    }
}
