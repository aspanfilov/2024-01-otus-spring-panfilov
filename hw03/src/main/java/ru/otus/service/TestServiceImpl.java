package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dao.QuestionDao;
import ru.otus.domain.Question;
import ru.otus.domain.Student;
import ru.otus.domain.TestResult;
import ru.otus.exceptions.QuestionReadException;
import ru.otus.service.IO.LocalizedIOService;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConverter questionConverter;

    @Override
    public TestResult executeTestFor(Student student) {
        greetStudent();
        var testResult = new TestResult(student);

        try {
            questionDao.findAll().forEach(question ->
                    handleQuestion(question, testResult));
        } catch (QuestionReadException e) {
            ioService.printLineLocalized(
                    "TestService.error.accessing.data");
        } catch (IllegalArgumentException e) {
            ioService.printLineLocalized(
                    "TestService.error.input.exceeded"
            );
        } catch (Exception e) {
            ioService.printLineLocalized("TestService.error.unexpected");
        }
        return testResult;
    }

    private void greetStudent() {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");
    }

    private void handleQuestion(Question question, TestResult testResult) {
        var questionText = questionConverter.convertQuestionToString(question);
        ioService.printLine(questionText);
        var answerNumber = askForAnswer(question);
        var isAnswerValid = isAnswerValid(question, answerNumber);
        testResult.applyAnswer(question, isAnswerValid);
    }

    private int askForAnswer(Question question) {
        return ioService.readIntForRangeWithPromptLocalized(1, question.answers().size(),
                "TestService.input.correct.answer",
                "TestService.input.range.error");
    }

    private boolean isAnswerValid(Question question, int answerNumber) {
        return question.answers().get(answerNumber - 1).isCorrect();
    }

}
