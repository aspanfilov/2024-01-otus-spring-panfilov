package ru.otus.exceptions;

public class QuestionFileReadException extends RuntimeException {
    public QuestionFileReadException(String message) {
        super((message));
    }
}
