package ru.otus.hw.dtos.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookBasicDto {
    private final Long id;

    private final String title;

    private final Long authorId;


    public BookBasicDto(@JsonProperty("id") Long id,
                        @JsonProperty("title") String title,
                        @JsonProperty("authorId") Long authorId) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
    }
}
