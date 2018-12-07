package ru.romanow.services.store.service;

import ru.romanow.services.store.domain.User;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface UserService {

    @Nonnull
    User getUserById(@Nonnull UUID userId);

    boolean checkUserExists(@Nonnull UUID userId);
}
