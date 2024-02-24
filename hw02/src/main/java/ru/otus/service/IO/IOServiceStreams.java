package ru.otus.service.IO;

import java.io.PrintStream;

public class IOServiceStreams implements IOService {

    private final PrintStream output;

    public IOServiceStreams(PrintStream outputStream) {
        this.output = outputStream;
    }

    @Override
    public void printLine(String s) {
        output.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        output.printf(s, args);
    }

    @Override
    public String readString() {
        return null;
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        return null;
    }

    @Override
    public int readIntForRange(int min, int max, String errorMessage) {
        return 0;
    }

    @Override
    public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
        return 0;
    }

}
