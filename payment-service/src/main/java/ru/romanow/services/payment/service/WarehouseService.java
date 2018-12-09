package ru.romanow.services.payment.service;

import ru.romanow.services.payment.model.enums.SizeChart;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseService {

    @Nonnull
    Optional<UUID> takeItem(@Nonnull UUID orderId, @Nonnull String model, @Nonnull SizeChart size);

    void returnItem(@Nonnull UUID orderId, @Nonnull UUID itemId);

    @Nonnull
    Optional<OrderWarrantyResponse> checkWarrantyItem(@Nonnull UUID itemId, @Nonnull OrderWarrantyRequest request);
}
