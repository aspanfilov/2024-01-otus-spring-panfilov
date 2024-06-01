package ru.otus.hw.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "username must not be blank")
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank(message = "password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Confirm Password must not be blank")
    @Size(min = 6, message = "Confirm Password must be at least 6 characters long")
    private String confirmPassword;
}
