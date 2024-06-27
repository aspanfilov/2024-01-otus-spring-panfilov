package ru.otus.hw.dtos.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookDetailDTO {

    private Long id;

    private String title;

    private String author;

    private List<String> genres;

    public BookDetailDTO(@JsonProperty("id") Long id,
                         @JsonProperty("title") String title,
                         @JsonProperty("author") String author,
                         @JsonProperty("genres") List<String> genres) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genres = genres;
    }

}
