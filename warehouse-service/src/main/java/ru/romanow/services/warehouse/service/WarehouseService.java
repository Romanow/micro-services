package ru.romanow.services.warehouse.service;

import ru.romanow.services.warehouse.model.ItemInfoResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface WarehouseService {

    @Nullable
    ItemInfoResponse getItemInfo(@Nonnull UUID itemId);
}
