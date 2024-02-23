package ru.otus.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Question {

    private final String questionText;

    private final List<Answer> answers;

    public Question(String questionText, List<Answer> answers) {
        this.questionText = questionText;
        this.answers = answers;
    }
}
