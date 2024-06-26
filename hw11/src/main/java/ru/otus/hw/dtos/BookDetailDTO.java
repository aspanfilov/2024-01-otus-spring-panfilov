package ru.otus.hw.dtos;

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

}
