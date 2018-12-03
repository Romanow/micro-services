package ru.romanow.services.store.service;

import ru.romanow.services.warehouse.model.OrderInfoResponse;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface WarehouseService {
    OrderInfoResponse getOrderInfo(@Nonnull UUID itemId);
}
