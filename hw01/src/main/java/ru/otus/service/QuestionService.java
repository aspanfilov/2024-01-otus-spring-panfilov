package ru.otus.service;

import ru.otus.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

    List<Question> getAll();

    Optional<Question> get(int id);

}
