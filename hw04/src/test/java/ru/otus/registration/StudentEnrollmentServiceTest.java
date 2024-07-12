package ru.otus.registration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.domain.Student;
import ru.otus.service.StudentService;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Класс StudentEnrollmentServiceImpl")
@SpringBootTest(classes = StudentEnrollmentServiceImpl.class)
public class StudentEnrollmentServiceTest {

    @MockBean
    private StudentService studentService;

    @MockBean
    private StudentRegistrationManager studentRegistrationService;

    @Autowired
    private StudentEnrollmentServiceImpl studentEnrollmentService;

    @DisplayName("enrollNewStudent регистрирует нового студента")
    @Test
    void enrollNewStudent() {
        Student student = new Student("Ivan", "Ivanov");

        when(studentService.identifyCurrentStudent()).thenReturn(student);

        studentEnrollmentService.enrollNewStudent();

        verify(studentService).identifyCurrentStudent();
        verify(studentRegistrationService).registerStudent(eq(student));
    }

}
