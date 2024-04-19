package ru.otus.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.otus.config.LocaleConfig;
import ru.otus.service.IO.LocalizedMessagesServiceImpl;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Класс LocalizedMessagesServiceImpl")
@ExtendWith(MockitoExtension.class)
public class LocalizedMessagesServiceImplTest {

    private static final String MESSAGE_CODE = "test.message";
    private static final String EXPECTED_MESSAGE = "test message";

    @Mock
    private LocaleConfig localeConfig;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    LocalizedMessagesServiceImpl localizedMessagesService;

    @BeforeEach
    void setUp() {
        when(localeConfig.getLocale()).thenReturn(Locale.ENGLISH);
    }

    @Test
    @DisplayName("getMessage возвращает корректное сообщения по коду с аргументами")
    void getMessage_ReturnsCorrectMessage() {
        Object[] args = {"param1", 123};
        when(messageSource.getMessage(MESSAGE_CODE, args, Locale.ENGLISH))
                .thenReturn(EXPECTED_MESSAGE);

        String actualMessage = localizedMessagesService.getMessage(MESSAGE_CODE, args);

        assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE);
    }

    @Test
    @DisplayName("getMessage возвращает корректное сообщения по коду без аргументов")
    void getMessage_ReturnsCorrectMessageWithoutArguments() {
        when(messageSource.getMessage(MESSAGE_CODE, new Object[0], Locale.ENGLISH))
                .thenReturn(EXPECTED_MESSAGE);

        String actualMessage = localizedMessagesService.getMessage(MESSAGE_CODE);

        assertThat(actualMessage).isEqualTo(EXPECTED_MESSAGE);
    }

}
