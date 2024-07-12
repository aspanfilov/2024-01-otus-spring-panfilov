package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.domain.Student;
import ru.otus.service.IO.LocalizedIOService;
import ru.otus.service.StudentServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Класс StudentServiceImpl")
@SpringBootTest(classes = StudentServiceImpl.class)
public class StudentServiceImplTest {

    @MockBean
    private LocalizedIOService inputService;

    @Autowired
    private StudentServiceImpl studentService;

    @Test
    @DisplayName("Корректно определяет текущего студента на основе ввода имени и фамилии")
    void shouldCorrectIdentifyCurrentStudent() {
        when(inputService.readStringWithPromptLocalized("StudentService.input.first.name"))
                .thenReturn("Ivan");
        when(inputService.readStringWithPromptLocalized("StudentService.input.last.name"))
                .thenReturn("Ivanov");

        Student student = studentService.identifyCurrentStudent();

        assertThat(student)
                .extracting(Student::firstName, Student::lastName)
                .containsExactly("Ivan", "Ivanov");
    }
}
