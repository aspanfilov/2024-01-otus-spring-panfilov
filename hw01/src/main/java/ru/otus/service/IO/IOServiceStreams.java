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

}
