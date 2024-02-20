package ru.otus.dao;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import ru.otus.config.QuizFileNameProvider;
import ru.otus.enums.QuestionHeaders;
import ru.otus.exceptions.QuestionFileProcessingException;
import ru.otus.domain.Question;
import ru.otus.service.QuestionConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class QuestionDaoCSV implements QuestionDao {

    private final QuestionConverter questionConverter;

    private final QuizFileNameProvider quizFileNameProvider;

    @Override
    public List<Question> getAll() {

        List<Question> questions;

        try (Reader reader = getQuestionsReader()) {
            Iterable<CSVRecord> records = parseQuestionsFromCSV(reader);
            questions = convertRecordsToQuestions(records);

        } catch (IOException ex) {
            throw new QuestionFileProcessingException(
                    String.format("Failed to process the question file %s due to an I/O error",
                            quizFileNameProvider.getQuizFileName()), ex);
        }
        return questions;
    }

    private Reader getQuestionsReader() {
        String quizFileName = quizFileNameProvider.getQuizFileName();
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(quizFileName);
        return new InputStreamReader(Objects.requireNonNull(resourceStream));
    }

    private Iterable<CSVRecord> parseQuestionsFromCSV(Reader reader) throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(QuestionHeaders.class)
                .setDelimiter(";")
                .build();
        return csvFormat.parse(reader);
    }

    private List<Question> convertRecordsToQuestions(Iterable<CSVRecord> records) {
        List<Question> questionsFromRecords = new ArrayList<>();
        for (CSVRecord record : records) {
            Question question = questionConverter.convertMapToQuestion(record.toMap());
            questionsFromRecords.add(question);
        }
        return questionsFromRecords;
    }

}
