package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.domain.Student;
import ru.otus.service.IO.InputService;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final InputService inputService;

    @Override
    public Student determineCurrentStudent() {
        var firstName = inputService.readStringWithPrompt("Please input your first name");
        var lastName = inputService.readStringWithPrompt("Please input your last name");
        return new Student(firstName, lastName);
    }
}
