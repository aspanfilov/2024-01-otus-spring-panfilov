package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.domain.Student;
import ru.otus.service.IO.InputService;
import ru.otus.service.StudentService;
import ru.otus.service.StudentServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("����� StudentServiceImpl")
@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    InputService inputService;

    @InjectMocks
    StudentService studentService;

    @Test
    @DisplayName("��������� ���������� �������� �������� �� ������ ����� ����� � �������")
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
