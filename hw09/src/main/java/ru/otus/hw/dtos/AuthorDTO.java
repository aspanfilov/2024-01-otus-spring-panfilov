package ru.otus.hw.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDTO {

    private Long id;

    @NotBlank(message = "full name must not be blank")
    private String fullName;

}
