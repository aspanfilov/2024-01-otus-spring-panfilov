package ru.otus.registration;

import ru.otus.domain.Student;

public class StudentContext {

    private static final ThreadLocal<Student> CURRENT_STUDENT = new ThreadLocal<>();

    public static void setCurrentStudent(Student student) {
        CURRENT_STUDENT.set(student);
    }

    public static Student getCurrentStudent() {
        return CURRENT_STUDENT.get();
    }

    public static void clear() {
        CURRENT_STUDENT.remove();
    }
}
