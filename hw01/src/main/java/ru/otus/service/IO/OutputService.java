package ru.otus.service.IO;

public interface OutputService {

    void printLine(String s);

    void printFormattedLine(String s, Object... args);

}
