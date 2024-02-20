package ru.otus.service;

import ru.otus.enums.QuestionHeaders;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;

import java.util.Map;

public class QuestionConverterImpl implements QuestionConverter {
    @Override
    public Question convertMapToQuestion(Map<String, String> data) {
        return Question.of(
                Integer.parseInt(data.get(QuestionHeaders.ID.name())),
                data.get(QuestionHeaders.QUESTION.name()),
                Integer.parseInt(data.get(QuestionHeaders.CORRECT_ANSWER_INDEX.name())),
                Answer.of(data.get(QuestionHeaders.ANSWER_1.name())),
                Answer.of(data.get(QuestionHeaders.ANSWER_2.name())),
                Answer.of(data.get(QuestionHeaders.ANSWER_3.name())),
                Answer.of(data.get(QuestionHeaders.ANSWER_4.name())),
                Answer.of(data.get(QuestionHeaders.ANSWER_5.name())));
    }

    @Override
    public String convertQuestionToString(Question question) {
        StringBuilder builder = new StringBuilder();
        builder.append(
                String.format("Question %d\n%s\n", question.getId(), question.getQuestionText()));
        question.getAnswers().forEach((answerId, answer) ->
                builder.append(String.format("%d %s\n", ++answerId, answer.getAnswerText())));

        return builder.toString();
    }
}
