package ru.otus.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.domain.Student;
import ru.otus.service.StudentService;

@Component
@RequiredArgsConstructor
public class StudentEnrollmentServiceImpl implements StudentEnrollmentService {

    private final StudentService studentService;

    private final StudentRegistrationManager studentRegistrationService;

    @Override
    public void enrollNewStudent() {
        Student student = studentService.identifyCurrentStudent();
        studentRegistrationService.registerStudent(student);
    }
}
