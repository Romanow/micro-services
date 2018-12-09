package ru.romanow.services.warranty.service;

import org.springframework.transaction.annotation.Transactional;
import ru.romanow.services.warranty.domain.Warranty;
import ru.romanow.services.warranty.modal.ItemWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;
import ru.romanow.services.warranty.modal.WarrantyInfoResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface WarrantyService {

    @Nonnull
    Warranty getWarrantyByItemId(@Nonnull UUID itemId);

    @Nullable
    WarrantyInfoResponse getWarrantyInfo(@Nonnull UUID itemId);

    @Nonnull
    OrderWarrantyResponse warrantyRequest(@Nonnull UUID itemId, @Nonnull ItemWarrantyRequest request);

    void startWarranty(@Nonnull UUID itemId);

    void stopWarranty(@Nonnull UUID itemId);
}
