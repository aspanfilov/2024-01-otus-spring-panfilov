package ru.otus.registration;

import ru.otus.domain.Student;

public interface StudentRegistrationManager {
    void registerStudent(Student student);

    boolean isStudentRegistered();

    Student getRegisteredStudent();

    void clearRegistration();
}



