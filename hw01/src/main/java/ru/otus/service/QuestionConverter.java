package ru.otus.service;

import ru.otus.domain.Question;

import java.util.Map;

public interface QuestionConverter {

    Question convertMapToQuestion(Map<String, String> data);

    String convertQuestionToString(Question question);
}
