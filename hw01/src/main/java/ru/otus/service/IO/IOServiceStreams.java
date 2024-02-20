package ru.otus.service.IO;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class IOServiceStreams implements IOService {

    private final PrintStream output;

    private final Scanner input;

    public IOServiceStreams(PrintStream outputStream, InputStream inputStream) {
        this.output = outputStream;
        this.input = new Scanner(inputStream);
    }

    @Override
    public void outputString(String string) {
        output.println(string);
    }

    @Override
    public int readInt() {
        return Integer.parseInt(input.nextLine());
    }

    @Override
    public int readIntWithPrompt(String prompt) {
        outputString(prompt);
        return Integer.parseInt(input.nextLine());
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        outputString(prompt);
        return input.nextLine();
    }
}
