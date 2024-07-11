package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.security.AuthorityGroup;

import java.util.Optional;

public interface AuthorityGroupRepository extends JpaRepository<AuthorityGroup, Long> {

    Optional<AuthorityGroup> findByName(String name);

}
