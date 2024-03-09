package ru.otus.service.IO;

public interface InputService {

    String readString();

    String readStringWithPrompt(String prompt);

    int readIntForRange(int min, int max, String errorMessage);

    int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage);

}
