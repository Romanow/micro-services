package ru.romanow.services.store.repostiory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.romanow.services.store.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository
        extends JpaRepository<User, Integer> {
    Optional<User> findByUid(UUID userId);
}
