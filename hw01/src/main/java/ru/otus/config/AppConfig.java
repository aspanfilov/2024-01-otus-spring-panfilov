package ru.otus.config;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppConfig implements QuizFileNameProvider {

    private final String quizFileName;

    @Override
    public String getQuizFileName() {
        return this.quizFileName;
    }
}
