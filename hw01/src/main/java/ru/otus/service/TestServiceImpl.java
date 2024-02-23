package ru.otus.service;

import lombok.RequiredArgsConstructor;
import ru.otus.dao.QuestionDao;
import ru.otus.exceptions.QuestionReadException;
import ru.otus.service.IO.IOService;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConverter questionConverter;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        try {
            questionDao.findAll().forEach(question -> {
                ioService.printLine(questionConverter.convertQuestionToString(question));
            });
        } catch (QuestionReadException e) {
            ioService.printLine(
                    "Failed to process the question file due to an I/O error");
        } catch (Exception e) {
            ioService.printLine("An unexpected error has occurred");
        }
    }

}
