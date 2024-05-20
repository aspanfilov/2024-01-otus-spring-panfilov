package ru.otus.hw.controllers.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.Errors;
import ru.otus.hw.config.SecurityConfig;
import ru.otus.hw.controllers.AuthController;
import ru.otus.hw.dtos.UserDTO;
import ru.otus.hw.services.RegistrationService;
import ru.otus.hw.validation.UserValidator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@DisplayName("Класс AuthController")
@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserValidator userValidator;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("При запросе GET /login должна вернуться страница логина")
    void testLoginPage() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    @DisplayName("При запросе GET /registration должна вернуться страница регистрации")
    void testRegistrationPage() throws Exception {
        mvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/registration"))
                .andExpect(model().attributeExists("userDTO"));
    }

    @Test
    @DisplayName("При POST запросе /registration с корректными данными должен произойти редирект на /auth/login")
    void testPerformRegistration() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setConfirmPassword("password");

        doNothing().when(userValidator).validate(any(), any());
        Mockito.doNothing().when(registrationService).register(any(UserDTO.class));

        mvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "testuser")
                        .param("password", "password")
                        .param("confirmPassword", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));

        verify(registrationService).register(any(UserDTO.class));
    }

    @Test
    @DisplayName("При запросе POST /registration с ошибками валидации должны быть показаны ошибки")
    void testPerformRegistrationWithErrors() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password");
        userDTO.setConfirmPassword("differentPassword");

        doAnswer(invocation -> {
            Errors errors = invocation.getArgument(1);
            errors.rejectValue("confirmPassword", "Match", "Passwords must match");
            return null;
        }).when(userValidator).validate(any(UserDTO.class), any(Errors.class));

        mvc.perform(post("/registration")
                        .param("username", userDTO.getUsername())
                        .param("password", userDTO.getPassword())
                        .param("confirmPassword", userDTO.getConfirmPassword())
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/auth/registration"))
                .andExpect(model().attributeHasFieldErrors("userDTO", "confirmPassword"));
    }
}
