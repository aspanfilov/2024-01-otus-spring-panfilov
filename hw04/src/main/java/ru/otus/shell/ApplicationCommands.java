package ru.otus.shell;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.registration.StudentEnrollmentService;
import ru.otus.registration.StudentRegistrationManager;
import ru.otus.service.IO.LocalizedMessagesService;
import ru.otus.service.TestRunner;

@ShellComponent(value = "Tester Commands")
public class ApplicationCommands {

    private final TestRunner testRunner;

    private final StudentEnrollmentService studentEnrollmentService;

    private final StudentRegistrationManager studentRegistrationService;

    private final LocalizedMessagesService localizedMessagesService;

    public ApplicationCommands(TestRunner testRunner,
                               StudentEnrollmentService studentEnrollmentService,
                               StudentRegistrationManager studentRegistrationService,
                               @Qualifier("localizedMessagesServiceImpl") LocalizedMessagesService localizedMessagesService) {
        this.testRunner = testRunner;
        this.studentEnrollmentService = studentEnrollmentService;
        this.studentRegistrationService = studentRegistrationService;
        this.localizedMessagesService = localizedMessagesService;
    }

    @ShellMethod(value = "Register student", key = {"r", "register"})
    public void registerStudent() {
        studentEnrollmentService.enrollNewStudent();
    }

    @ShellMethod(value = "Unregister student", key = {"u", "unregister"})
    public void unregisterStudent() {
        studentRegistrationService.clearRegistration();
    }

    @ShellMethod(value = "Start test", key = {"s", "start"})
    @ShellMethodAvailability(value = "isStudentRegistered")
    public void startTest() {
        testRunner.run();
    }

    private Availability isStudentRegistered() {
        return studentRegistrationService.isStudentRegistered()
                ? Availability.available()
                : Availability.unavailable(localizedMessagesService.getMessage(
                        "ApplicationCommands.student.not.registered"));
    }

}
