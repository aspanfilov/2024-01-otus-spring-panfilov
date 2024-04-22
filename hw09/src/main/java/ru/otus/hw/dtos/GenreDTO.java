package ru.otus.hw.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenreDTO {

    private Long id;

    @NotBlank(message = "name must not be blank")
    private String name;

}
