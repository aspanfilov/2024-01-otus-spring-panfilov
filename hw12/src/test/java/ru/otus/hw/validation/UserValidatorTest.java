package ru.otus.hw.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import ru.otus.hw.dtos.UserDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Класс UserValidator")
@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private UserDetailsManager userDetailsManager;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    @DisplayName("Должен добавить ошибку, если пароли не совпадают")
    void shouldAddErrorIfPasswordsDoNotMatch() {
        UserDTO userDTO = new UserDTO(null, "testuser", "password", "differentPassword");
        Errors errors = new BeanPropertyBindingResult(userDTO, "userDTO");

        userValidator.validate(userDTO, errors);

        assertThat(errors.getErrorCount()).isEqualTo(1);
        assertThat(errors.getFieldError("confirmPassword")).isNotNull();
        assertThat(errors.getFieldError("confirmPassword").getCode()).isEqualTo("Match");
        assertThat(errors.getFieldError("confirmPassword").getDefaultMessage()).isEqualTo("Passwords must match");
    }

    @Test
    @DisplayName("Должен добавить ошибку, если имя пользователя уже занято")
    void shouldAddErrorIfUsernameAlreadyExists() {
        UserDTO userDTO = new UserDTO(null, "testuser", "password", "password");
        Errors errors = new BeanPropertyBindingResult(userDTO, "userDTO");

        when(userDetailsManager.userExists("testuser")).thenReturn(true);

        userValidator.validate(userDTO, errors);

        assertThat(errors.getErrorCount()).isEqualTo(1);
        assertThat(errors.getFieldError("username")).isNotNull();
        assertThat(errors.getFieldError("username").getCode()).isEqualTo("Duplicate");
        assertThat(errors.getFieldError("username").getDefaultMessage()).isEqualTo("Username is already in use");
    }

    @Test
    @DisplayName("Не должен добавлять ошибок, если данные валидны")
    void shouldNotAddErrorsIfDataIsValid() {
        UserDTO userDTO = new UserDTO(null, "testuser", "password", "password");
        Errors errors = new BeanPropertyBindingResult(userDTO, "userDTO");

        when(userDetailsManager.userExists("testuser")).thenReturn(false);

        userValidator.validate(userDTO, errors);

        assertThat(errors.getErrorCount()).isEqualTo(0);
    }
}
