package ru.romanow.services.store.service;

import ru.romanow.services.warranty.modal.WarrantyInfoResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public interface WarrantyService {

    @Nonnull
    Optional<WarrantyInfoResponse> getItemWarrantyInfo(@Nonnull UUID itemId);
}
