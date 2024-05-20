package ru.otus.hw.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.otus.hw.dtos.UserDTO;
import ru.otus.hw.services.UserService;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;

        validatePasswords(userDTO, errors);
        validateUsername(userDTO, errors);
    }

    private void validatePasswords(UserDTO userDTO, Errors errors) {
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())) {
            errors.rejectValue("confirmPassword", "Match", "Passwords must match");
        }
    }

    private void validateUsername(UserDTO userDTO, Errors errors) {
        if (userService.findByUsername(userDTO.getUsername()).isPresent()) {
            errors.rejectValue("username", "Duplicate", "Username is already in use");
        }
    }
}
