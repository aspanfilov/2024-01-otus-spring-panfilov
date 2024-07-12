package ru.otus.registration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.domain.Student;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Класс StudentRegistrationManagerImpl")
@SpringBootTest(classes = StudentRegistrationManagerImpl.class)
public class StudentRegistrationManagerTest {

    @Autowired
    private StudentRegistrationManagerImpl studentRegistrationManager;

    private Student student;

    @BeforeEach
    void setUp() {
        student = new Student("Ivan", "Ivanov");
    }

    @AfterEach
    void tearDown() {
        StudentContext.clear();
    }

    @Test
    @DisplayName("registerStudent регистрирует студента")
    void registerStudent() {
        studentRegistrationManager.registerStudent(student);

        assertThat(StudentContext.getCurrentStudent())
                .isNotNull()
                .isEqualTo(StudentContext.getCurrentStudent());
    }

    @Test
    @DisplayName("isStudentRegistered возвращает true, когда студент зарегистрирован")
    void isStudentRegistered_returnsTrue() {
        studentRegistrationManager.registerStudent(student);
        assertThat(studentRegistrationManager.isStudentRegistered())
                .isTrue();
    }

    @Test
    @DisplayName("isStudentRegistered возвращает false, когда студент не зарегистрирован")
    void isStudentRegistered_returnsFalse() {
        assertThat(studentRegistrationManager.isStudentRegistered())
                .isFalse();
    }

    @Test
    @DisplayName("getRegisteredStudent возвращает зарегистрированного студента")
    void getRegisteredStudent() {
        studentRegistrationManager.registerStudent(student);
        assertThat(studentRegistrationManager.getRegisteredStudent())
                .isEqualTo(student);
    }

    @Test
    @DisplayName("clearRegistration очищает регистрацию студента")
    void clearRegistration() {
        studentRegistrationManager.registerStudent(student);
        studentRegistrationManager.clearRegistration();
        assertThat(StudentContext.getCurrentStudent()).isNull();
    }

}
