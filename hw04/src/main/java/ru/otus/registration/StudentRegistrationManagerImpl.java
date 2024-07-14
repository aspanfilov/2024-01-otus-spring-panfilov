package ru.otus.registration;

import org.springframework.stereotype.Component;
import ru.otus.domain.Student;

@Component
public class StudentRegistrationManagerImpl implements StudentRegistrationManager {

    @Override
    public void registerStudent(Student student) {
        StudentContext.setCurrentStudent(student);
    }

    @Override
    public boolean isStudentRegistered() {
        return StudentContext.getCurrentStudent() != null;
    }

    @Override
    public Student getRegisteredStudent() {
        return StudentContext.getCurrentStudent();
    }

    @Override
    public void clearRegistration() {
        StudentContext.clear();
    }
}
