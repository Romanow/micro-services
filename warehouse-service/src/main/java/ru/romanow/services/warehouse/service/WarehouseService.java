package ru.romanow.services.warehouse.service;

import ru.romanow.services.warehouse.model.ItemInfoResponse;
import ru.romanow.services.warehouse.model.OrderItemInfoResponse;
import ru.romanow.services.warehouse.model.OrderItemRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public interface WarehouseService {

    @Nullable
    OrderItemInfoResponse getItemInfo(@Nonnull UUID itemId);

    @Nonnull
    List<ItemInfoResponse> getItemsInfo();

    @Nonnull
    UUID takeItem(@Nonnull OrderItemRequest request);

    void returnItem(@Nonnull UUID itemId);

    @Nonnull
    OrderWarrantyResponse warrantyRequest(@Nonnull UUID itemId, @Nonnull OrderWarrantyRequest request);
}
