package ru.romanow.services.warehouse.service;

import org.springframework.transaction.annotation.Transactional;
import ru.romanow.services.warehouse.domain.OrderItem;
import ru.romanow.services.warehouse.model.ItemInfoResponse;
import ru.romanow.services.warehouse.model.OrderItemInfoResponse;
import ru.romanow.services.warehouse.model.OrderItemRequest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public interface WarehouseService {

    @Nullable
    OrderItemInfoResponse getItemInfo(@Nonnull UUID itemId);

    @Nonnull
    OrderItem getOrderItem(@Nonnull UUID itemId);

    @Nonnull
    List<ItemInfoResponse> getItemsInfo();

    @Nonnull
    UUID takeItem(@Nonnull OrderItemRequest request);

    void returnItem(@Nonnull UUID itemId);

    int checkItemAvailableCount(@Nonnull UUID itemId);
}