package ru.otus.service.IO;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.config.LocaleConfig;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class LocalizedMessagesServiceImpl implements LocalizedMessagesService {

    private final LocaleConfig localeConfig;

    private final MessageSource messageSource;

    @Override
    public String getMessage(String code, Object... args) {

        var argsArr = Arrays.stream(args)
                .peek(arg -> {
                    if (arg == null) {
                        throw new IllegalArgumentException("Null argument detected for message code: " + code);
                    }
                })
                .map(Object::toString)
                .toArray(String[]::new);

        return messageSource.getMessage(code, argsArr, localeConfig.getLocale());

    }
}
