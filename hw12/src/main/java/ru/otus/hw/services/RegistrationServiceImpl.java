package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.UserDTO;
import ru.otus.hw.models.User;
import ru.otus.hw.security.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserDetailsManager userDetailsManager;

    @Transactional
    @Override
    public void register(UserDTO userDTO) {

        if (userDetailsManager.userExists(userDTO.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        userDetailsManager.createUser(customUserDetails);
    }
}
