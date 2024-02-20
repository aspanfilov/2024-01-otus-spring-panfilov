package ru.otus.service;

import ru.otus.domain.Question;
import ru.otus.service.IO.IOService;

import java.util.ArrayList;
import java.util.List;

public class QuizProcessorImpl implements QuizProcessor {

    private final List<Question> questions;

    private final QuestionService questionService;

    private final QuestionConverter questionConverter;

    private final IOService ioService;

    public QuizProcessorImpl(QuestionService questionService, QuestionConverter questionConverter, IOService ioService) {
        questions = new ArrayList<>();
        this.questionService = questionService;
        this.questionConverter = questionConverter;
        this.ioService = ioService;
    }

    @Override
    public void loadQuestions() {
        questions.clear();
        questions.addAll(questionService.getAll());
    }

    @Override
    public void printQuestions() {
        questions.forEach(q -> ioService.outputString(questionConverter.convertQuestionToString(q)));
    }
}
