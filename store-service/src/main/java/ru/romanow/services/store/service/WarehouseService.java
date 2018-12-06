package ru.romanow.services.store.service;

import ru.romanow.services.warehouse.model.OrderInfoResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface WarehouseService {

    @Nullable
    OrderInfoResponse getOrderInfo(@Nonnull UUID itemId);
}
