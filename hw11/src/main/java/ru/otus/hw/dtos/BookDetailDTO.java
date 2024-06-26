package ru.otus.hw.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookDetailDTO {

    private Long id;

//    @NotBlank(message = "title cannot be blank")
    private String title;

//    @NotNull(message = "author must be selected")
    private String author;

//    @NotEmpty(message = "at least one genre must be selected")
    private List<String> genres;

}
