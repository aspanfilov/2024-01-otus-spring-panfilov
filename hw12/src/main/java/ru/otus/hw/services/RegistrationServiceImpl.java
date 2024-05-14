package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.UserDTO;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void register(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        if (userService.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        userService.insert(username, passwordEncoder.encode(password));
    }
}
