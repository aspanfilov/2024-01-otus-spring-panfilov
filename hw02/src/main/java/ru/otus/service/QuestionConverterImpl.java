package ru.otus.service;

import org.springframework.stereotype.Component;
import ru.otus.domain.Question;

import java.util.stream.IntStream;

@Component
public class QuestionConverterImpl implements QuestionConverter {

    @Override
    public String convertQuestionToString(Question question) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("%s\n", question.getQuestionText()));

        IntStream.range(0, question.getAnswers().size())
                .forEach(i -> builder.append(
                        String.format("%d %s\n", i + 1, question.getAnswers().get(i).getText())));

        return builder.toString();
    }
}