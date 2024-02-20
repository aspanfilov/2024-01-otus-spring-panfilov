package ru.otus.service;

import lombok.RequiredArgsConstructor;
import ru.otus.dao.QuestionDao;
import ru.otus.domain.Question;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    @Override
    public List<Question> getAll() {
        return questionDao.getAll().stream()
                .map(Question::copy)
                .collect(Collectors.toList());
    }

    public Optional<Question> get(int id) {
        return questionDao.getAll().stream()
                .filter(question -> question.getId() == id)
                .findFirst()
                .map(Question::copy);
    }

}
