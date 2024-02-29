package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.domain.Student;
import ru.otus.service.IO.InputService;
import ru.otus.service.StudentServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Класс StudentServiceImpl")
@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private InputService inputService;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    @DisplayName("Корректно определяет текущего студента на основе ввода имени и фамилии")
    void shouldCorrectDetermineCurrentStudent() {
        when(inputService.readStringWithPrompt("Please input your first name"))
                .thenReturn("Ivan");
        when(inputService.readStringWithPrompt("Please input your last name"))
                .thenReturn("Ivanov");

        Student student = studentService.determineCurrentStudent();

        assertThat(student)
                .extracting(Student::firstName, Student::lastName)
                .containsExactly("Ivan", "Ivanov");
    }
}
