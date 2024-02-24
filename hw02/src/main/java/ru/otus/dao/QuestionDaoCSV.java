package ru.otus.dao;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.config.TestFileNameProvider;
import ru.otus.dao.dto.QuestionDto;
import ru.otus.domain.Question;
import ru.otus.exceptions.QuestionFileReadException;
import ru.otus.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QuestionDaoCSV implements QuestionDao {

    private final TestFileNameProvider testFileNameProvider;

    @Override
    public List<Question> findAll() {
        try (CSVReader csvReader = getQuestionCSVReader()) {
            return new CsvToBeanBuilder<QuestionDto>(csvReader)
                    .withType(QuestionDto.class)
                    .build()
                    .parse()
                    .stream().map(QuestionDto::toDomainObject)
                    .toList();
        } catch (IOException e) {
            throw new QuestionReadException(String.format(
                    "Failed to process the question file %s due to an I/O error",
                    testFileNameProvider.getTestFileName()), e);
        }
    }

    private CSVReader getQuestionCSVReader() {
        return new CSVReaderBuilder(getQuestionsReader())
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(';')
                        .build())
                .withSkipLines(1)
                .build();
    }

    private Reader getQuestionsReader() {
        String testFileName = testFileNameProvider.getTestFileName();
        InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(testFileName);
        if (resourceStream == null) {
            throw new QuestionFileReadException(String.format(
                    "Resource file %s not found", testFileName));
        }
        return new InputStreamReader(resourceStream);
    }

}
