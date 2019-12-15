package ru.romanow.services.warehouse.service;

import ru.romanow.services.warehouse.domain.OrderItem;
import ru.romanow.services.warehouse.model.OrderItemInfoResponse;
import ru.romanow.services.warehouse.model.OrderItemRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface WarehouseService {

    @Nullable
    OrderItemInfoResponse getItemInfo(@Nonnull UUID itemId);

    @Nonnull
    OrderItem getOrderItem(@Nonnull UUID itemId);

    @Nonnull
    UUID takeItem(@Nonnull OrderItemRequest request);

    void returnItem(@Nonnull UUID itemId);

    int checkItemAvailableCount(@Nonnull UUID itemId);
}