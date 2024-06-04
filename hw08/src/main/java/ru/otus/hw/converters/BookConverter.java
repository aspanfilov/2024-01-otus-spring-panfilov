package ru.otus.hw.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.exceptions.JsonConversationException;
import ru.otus.hw.models.Book;

@Component
@RequiredArgsConstructor
public class BookConverter {

    private final ObjectMapper objectMapper;

    public String bookToString(Book book) {
        try {
            return objectMapper.writeValueAsString(book);
        } catch (JsonProcessingException e) {
            throw new JsonConversationException("Error converting book to JSON", e);
        }
    }
}

