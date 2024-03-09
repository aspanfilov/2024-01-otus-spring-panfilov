package ru.otus.service;

import ru.otus.domain.Question;

public interface QuestionConverter {
    String convertQuestionToString(Question question);
}
