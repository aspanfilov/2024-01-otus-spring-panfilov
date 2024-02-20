package ru.otus.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.dao.QuestionDao;
import ru.otus.service.QuestionServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Сервис вопросов")
public class QuestionServiceImplTest {

    @Mock
    private QuestionDao questionDao;

    private QuestionServiceImpl questionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        questionService = new QuestionServiceImpl(questionDao);
    }

    @DisplayName("метод getAll возвращает копию всех вопросов")
    @Test
    void getAllShouldReturnCopyOfAllQuestions() {
        Question question1 = Question.of(1, "Question 1", 1, generateAnswers());
        Question question2 = Question.of(2, "Question 2", 2, generateAnswers());
        List<Question> mockQuestions = Arrays.asList(question1, question2);

        when(questionDao.getAll()).thenReturn(mockQuestions);

        List<Question> questions = questionService.getAll();

        assertEquals(questions, mockQuestions);
        assertNotSame(questions, mockQuestions);
    }

    @DisplayName("метод get возвращает копию вопроса")
    @Test
    void getShouldReturnQuestionById() {
        Question mockQuestion = Question.of(1, "Question 1", 1, generateAnswers());
        when(questionDao.getAll()).thenReturn(List.of(mockQuestion));

        Optional<Question> question = questionService.get(1);

        assertEquals(question.get(), mockQuestion);
        assertNotSame(question.get(), mockQuestion);
    }

    private Answer[] generateAnswers() {
        return new Answer[]{
                Answer.of("Answer 1"),
                Answer.of("Answer 3"),
                Answer.of("Answer 3"),
                Answer.of("Answer 4"),
                Answer.of("Answer 5")};

    }

}
