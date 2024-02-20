package ru.otus.exceptions;

public class QuestionFileProcessingException extends RuntimeException {
    public QuestionFileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
