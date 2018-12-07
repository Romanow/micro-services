package ru.romanow.services.warranty.service;

import ru.romanow.services.warranty.modal.WarrantyInfoResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface WarrantyService {

    @Nullable
    WarrantyInfoResponse getWarrantyInfo(@Nonnull UUID itemId);
}
