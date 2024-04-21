package ru.otus.hw.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookDTO {

    private Long id;

    private String title;

    private AuthorDTO author;

    private List<GenreDTO> genres;

}
