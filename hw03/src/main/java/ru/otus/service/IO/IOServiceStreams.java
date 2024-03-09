package ru.otus.service.IO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.config.IOConfig;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class IOServiceStreams implements IOService {

    private final PrintStream printStream;

    private final Scanner scanner;

    private final IOConfig ioConfig;

    public IOServiceStreams(@Value("#{T(System).out}") PrintStream outputStream,
                            @Value("#{T(System).in}") InputStream inputStream,
                            IOConfig ioConfig) {
        this.printStream = outputStream;
        this.scanner = new Scanner(inputStream);
        this.ioConfig = ioConfig;
    }

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @Override
    public String readString() {
        return scanner.nextLine();
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        printLine(prompt);
        return readString();
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        for (int i = 0; i < ioConfig.getMaxInputAttempts(); i++) {
            try {
                var stringValue = scanner.nextLine();
                int intValue = Integer.parseInt(stringValue);
                if (intValue < min || intValue > max) {
                    throw new IllegalArgumentException();
                }
                return intValue;
            } catch (IllegalArgumentException e) {
                printLine(errorMessage);
            }
        }
        throw new IllegalArgumentException("Error during reading int value");
    }

    @Override
    public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        printLine(prompt);
        return readIntForRange(min, max, errorMessage);
    }

}
