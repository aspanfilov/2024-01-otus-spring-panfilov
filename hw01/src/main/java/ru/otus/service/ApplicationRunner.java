package ru.otus.service;

import lombok.RequiredArgsConstructor;
import ru.otus.exceptions.CorrectAnswerIndexOutOfBoundsException;
import ru.otus.exceptions.QuestionFileProcessingException;
import ru.otus.service.IO.OutputService;

@RequiredArgsConstructor
public class ApplicationRunner {

    private final QuizProcessor quizProcessor;

    private final OutputService outputService;

    public void run() {
        try {
            quizProcessor.loadQuestions();
            quizProcessor.printQuestions();
        } catch (QuestionFileProcessingException e) {
            outputService.outputString(
                    "Failed to process the question file due to an I/O error.");
        } catch (CorrectAnswerIndexOutOfBoundsException e) {
            outputService.outputString(
                    "Question creation error: Correct answer index out of allowed answer options range.");
        }
    }
}
