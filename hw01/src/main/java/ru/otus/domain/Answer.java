package ru.otus.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@Getter
@RequiredArgsConstructor
public class Answer {

    private final String answerText;

    public static Answer of(String answerText) {
        return new Answer(answerText);
    }

    public Answer copy() {
        return new Answer(answerText);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Answer answer = (Answer) o;
        return Objects.equals(answerText, answer.answerText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answerText);
    }
}
