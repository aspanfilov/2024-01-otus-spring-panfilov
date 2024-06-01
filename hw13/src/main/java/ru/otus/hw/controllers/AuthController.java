package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.dtos.UserDTO;
import ru.otus.hw.services.RegistrationService;
import ru.otus.hw.validation.UserValidator;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserValidator userValidator;

    private final RegistrationService registrationService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("userDTO") UserDTO userDTO) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("userDTO") @Valid UserDTO userDTO,
                                      BindingResult bindingResult) {

        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }

        registrationService.register(userDTO);

        return "redirect:/auth/login";
    }
}
