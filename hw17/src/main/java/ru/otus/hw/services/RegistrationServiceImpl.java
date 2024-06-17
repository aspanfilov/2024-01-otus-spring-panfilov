package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.UserDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.User;
import ru.otus.hw.repositories.AuthorityGroupRepository;
import ru.otus.hw.security.AuthorityGroup;
import ru.otus.hw.security.CustomUserDetails;
import ru.otus.hw.security.AuthorityGroupRole;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserDetailsManager userDetailsManager;

    private final AuthorityGroupRepository authorityGroupRepository;

    @Transactional
    @Override
    public void register(UserDTO userDTO) {

        if (userDetailsManager.userExists(userDTO.getUsername())) {
            throw new IllegalArgumentException("User already exists");
        }

        AuthorityGroup guest = authorityGroupRepository.findByName(AuthorityGroupRole.GUEST.name())
                .orElseThrow(() -> new EntityNotFoundException("Authority group GUEST not found"));

        User user = User.builder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .authorityGroups(Set.of(guest))
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        userDetailsManager.createUser(customUserDetails);
    }
}
