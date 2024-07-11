package ru.otus.registration;

import ru.otus.domain.Student;

public class StudentContext {

    private static final ThreadLocal<Student> currentStudent = new ThreadLocal<>();

    public static void setCurrentStudent(Student student) {
        currentStudent.set(student);
    }

    public static Student getCurrentStudent() {
        return currentStudent.get();
    }

    public static void clear() {
        currentStudent.remove();
    }
}
