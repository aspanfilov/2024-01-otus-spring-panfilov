package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.config.LocaleConfig;
import ru.otus.service.IO.LocalizedMessagesServiceImpl;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Класс LocalizedMessagesServiceImpl")
@SpringBootTest(classes = LocalizedMessagesServiceImpl.class)
@Import(LocalizedMessagesServiceImplTest.TestConfig.class)
public class LocalizedMessagesServiceImplTest {

    private static final String MESSAGE_CODE = "test.message";
    private static final String EXPECTED_MESSAGE_EN = "test message";
    private static final String EXPECTED_MESSAGE_RU = "тестовое сообщение";

    @Configuration
    static class TestConfig {
        @Bean
        public MessageSource messageSource() {
            ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
            messageSource.setBasename("classpath:messages");
            messageSource.setDefaultEncoding("UTF-8");
            messageSource.setFallbackToSystemLocale(false);
            return messageSource;
        }
    }

    @MockBean
    private LocaleConfig localeConfig;

    @Autowired
    private LocalizedMessagesServiceImpl localizedMessagesService;

    @Test
    @DisplayName("getMessage возвращает корректное сообщение на английском по коду с аргументами")
    void getMessage_ReturnsCorrectMessage_En() {
        when(localeConfig.getLocale()).thenReturn(Locale.ENGLISH);
        Object[] args = {"param1", 123};
        String actualMessage = localizedMessagesService.getMessage(MESSAGE_CODE, args);

        assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE_EN);
    }

    @Test
    @DisplayName("getMessage возвращает корректное сообщение на английском по коду без аргументов")
    void getMessage_ReturnsCorrectMessageWithoutArguments_En() {
        when(localeConfig.getLocale()).thenReturn(Locale.ENGLISH);
        String actualMessage = localizedMessagesService.getMessage(MESSAGE_CODE);

        assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE_EN);
    }

    @Test
    @DisplayName("getMessage возвращает корректное сообщение на русском по коду с аргументами")
    void getMessage_ReturnsCorrectMessage_Ru() {
        when(localeConfig.getLocale()).thenReturn(new Locale("ru", "RU"));
        Object[] args = {"param1", 123};
        String actualMessage = localizedMessagesService.getMessage(MESSAGE_CODE, args);

        assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE_RU);
    }

    @Test
    @DisplayName("getMessage возвращает корректное сообщение на русском по коду без аргументов")
    void getMessage_ReturnsCorrectMessageWithoutArguments_Ru() {
        when(localeConfig.getLocale()).thenReturn(new Locale("ru", "RU"));
        String actualMessage = localizedMessagesService.getMessage(MESSAGE_CODE);

        assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE_RU);
    }

}
