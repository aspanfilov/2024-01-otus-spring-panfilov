package ru.otus.domain;

import lombok.Getter;
import ru.otus.exceptions.CorrectAnswerIndexOutOfBoundsException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class Question {

    private final int id;

    private final String questionText;

    private final Map<Integer, Answer> answers;

    private final int correctAnswerIndex;

    private Question(int id, String questionText, Map<Integer, Answer> answers, int correctAnswerIndex) {
        this.id = id;
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public static Question of(int id, String questionText, int correctAnswerIndex, Answer... answerOptions) {
        if (correctAnswerIndex < 1 || correctAnswerIndex > answerOptions.length) {
            throw new CorrectAnswerIndexOutOfBoundsException(
                    "The correct answer index is out of bounds for the provided answer options");
        }

        Map<Integer, Answer> answers = new HashMap<>();
        for (int i = 0; i < answerOptions.length; i++) {
            answers.put(i, answerOptions[i]);
        }
        return new Question(id, questionText, answers, correctAnswerIndex);
    }

    public Question copy() {
        Map<Integer, Answer> copiedAnswers = new HashMap<>();
        for (Map.Entry<Integer, Answer> entry : answers.entrySet()) {
            copiedAnswers.put(entry.getKey(), entry.getValue().copy());
        }
        return new Question(id, questionText, copiedAnswers, correctAnswerIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return id == question.id &&
                correctAnswerIndex == question.correctAnswerIndex &&
                Objects.equals(questionText, question.questionText) &&
                Objects.equals(answers, question.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, questionText, answers, correctAnswerIndex);
    }
}
