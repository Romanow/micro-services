package ru.romanow.services.store.service;

import ru.romanow.services.warehouse.model.OrderItemInfoResponse;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public interface WarehouseService {

    @Nonnull
    Optional<OrderItemInfoResponse> getItemInfo(@Nonnull UUID itemId);
}
