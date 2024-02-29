package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.dao.QuestionDao;
import ru.otus.domain.Question;
import ru.otus.domain.Student;
import ru.otus.domain.TestResult;
import ru.otus.exceptions.QuestionReadException;
import ru.otus.service.IO.IOService;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

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
            ioService.printLine(
                    "An error occurred while accessing the question data. Please check the data source and try again");
        } catch (IllegalArgumentException e) {
            ioService.printLine(
                    "Maximum input attempts exceeded. The test has ended with some questions left unanswered"
            );
        } catch (Exception e) {
            ioService.printLine("An unexpected error has occurred");
        }
        return testResult;
    }

    private void greetStudent() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
    }

    private void handleQuestion(Question question, TestResult testResult) {
        var questionText = questionConverter.convertQuestionToString(question);
        ioService.printLine(questionText);
        var answerNumber = askForAnswer(question);
        var isAnswerValid = isAnswerValid(question, answerNumber);
        testResult.applyAnswer(question, isAnswerValid);
    }

    private int askForAnswer(Question question) {
        return ioService.readIntForRangeWithPrompt(1, question.answers().size(),
                "Please input correct answer number",
                String.format("A numeric value within the range from %d to %d is required. Please, try again.",
                        1, question.answers().size()));
    }

    private boolean isAnswerValid(Question question, int answerNumber) {
        return question.answers().get(answerNumber - 1).isCorrect();
    }

}
