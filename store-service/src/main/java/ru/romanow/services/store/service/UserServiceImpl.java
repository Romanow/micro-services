package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanow.services.store.domain.User;
import ru.romanow.services.store.repostiory.UserRepository;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.UUID;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class UserServiceImpl
        implements UserService {
    private final UserRepository userRepository;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public User getUserById(@Nonnull UUID userId) {
        return userRepository.findByUid(userId)
                .orElseThrow(() -> new EntityNotFoundException(format("User with id '%s' not found", userId)));
    }
}
