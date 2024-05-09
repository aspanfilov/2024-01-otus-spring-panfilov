package ru.otus.service.IO;

public interface LocalizedMessagesService {
    String getMessage(String code, Object ...args);
}
