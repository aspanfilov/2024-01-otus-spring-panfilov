package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.otus.config.LocaleConfig;
import ru.otus.service.IO.LocalizedMessagesServiceImpl;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Класс LocalizedMessagesServiceImpl")
@SpringBootTest(classes = {LocalizedMessagesServiceImpl.class, MessageSourceAutoConfiguration.class})
public class LocalizedMessagesServiceImplTest {

    private static final String MESSAGE_CODE = "test.message";
    private static final String EXPECTED_MESSAGE_EN = "test message";
    private static final String EXPECTED_MESSAGE_RU = "тестовое сообщение";

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private LocaleConfig localeConfig;

    @Autowired
    private LocalizedMessagesServiceImpl localizedMessagesService;


    @Test
    @DisplayName("getMessage возвращает корректное сообщение на английском по коду с аргументами")
    void getMessage_ReturnsCorrectMessage_En() {
        Object[] args = {"param1", 123};

        when(localeConfig.getLocale()).thenReturn(Locale.ENGLISH);
        when(messageSource.getMessage(MESSAGE_CODE, args, Locale.ENGLISH))
                .thenReturn(EXPECTED_MESSAGE_EN);

        String actualMessage = localizedMessagesService.getMessage(MESSAGE_CODE, args);

        assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE_EN);
    }

    @Test
    @DisplayName("getMessage возвращает корректное сообщение на английском по коду без аргументов")
    void getMessage_ReturnsCorrectMessageWithoutArguments_En() {
        when(localeConfig.getLocale()).thenReturn(Locale.ENGLISH);
        when(messageSource.getMessage(MESSAGE_CODE, new Object[0], Locale.ENGLISH))
                .thenReturn(EXPECTED_MESSAGE_EN);

        String actualMessage = localizedMessagesService.getMessage(MESSAGE_CODE);

        assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE_EN);
    }

    @Test
    @DisplayName("getMessage возвращает корректное сообщение на русском по коду с аргументами")
    void getMessage_ReturnsCorrectMessage_Ru() {
        Object[] args = {"param1", 123};
        Locale ruLocale = new Locale("ru", "RU");

        when(localeConfig.getLocale()).thenReturn(ruLocale);
        when(messageSource.getMessage(MESSAGE_CODE, args, ruLocale))
                .thenReturn(EXPECTED_MESSAGE_RU);

        String actualMessage = localizedMessagesService.getMessage(MESSAGE_CODE, args);

        assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE_RU);
    }

    @Test
    @DisplayName("getMessage возвращает корректное сообщение на русском по коду без аргументов")
    void getMessage_ReturnsCorrectMessageWithoutArguments_Ru() {
        Locale ruLocale = new Locale("ru", "RU");

        when(localeConfig.getLocale()).thenReturn(ruLocale);
        when(messageSource.getMessage(MESSAGE_CODE, new Object[0], ruLocale))
                .thenReturn(EXPECTED_MESSAGE_RU);

        String actualMessage = localizedMessagesService.getMessage(MESSAGE_CODE);

        assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE_RU);
    }

}
