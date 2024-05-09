package ru.otus.hw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookDTO {

    private Long id;

    @NotBlank(message = "title cannot be blank")
    private String title;

    @NotNull(message = "author must be selected")
    private AuthorDTO author;

    @NotEmpty(message = "at least one genre must be selected")
    private List<GenreDTO> genres;

}
