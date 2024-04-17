package ru.otus.service;

import org.springframework.stereotype.Component;
import ru.otus.domain.Question;

import java.util.stream.IntStream;

@Component
public class QuestionConverterImpl implements QuestionConverter {

    @Override
    public String convertQuestionToString(Question question) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("%s\n", question.text()));

        IntStream.range(0, question.answers().size())
                .forEach(i -> builder.append(
                        "%d %s\n".formatted(i + 1, question.answers().get(i).text())));

        return builder.toString();
    }
}