package ru.otus.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.registration.StudentRegistrationManager;

@Component
@RequiredArgsConstructor
public class TestRunnerImpl implements TestRunner {

    private final StudentRegistrationManager studentRegistrationService;

    private final TestService testService;

    private final ResultService resultService;

    @Override
    public void run() {
        var student = studentRegistrationService.getRegisteredStudent();
        var testResult = testService.executeTestFor(student);
        resultService.showResult(testResult);
    }
}
