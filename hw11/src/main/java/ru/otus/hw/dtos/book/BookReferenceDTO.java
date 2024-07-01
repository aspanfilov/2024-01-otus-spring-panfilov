package ru.otus.hw.dtos.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReferenceDTO {

    private Long id;

    @NotBlank(message = "title cannot be blank")
    private String title;

    @NotNull(message = "author must be selected")
    private Long authorId;

    @NotEmpty(message = "at least one genre must be selected")
    private Set<Long> genreIds;
}
