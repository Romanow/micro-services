package ru.romanow.services.payment.service;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public interface WarrantyService {
    void startWarranty(@Nonnull UUID itemId);

    void stopWarranty(@Nonnull UUID itemId);
}
