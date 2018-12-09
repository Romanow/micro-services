package ru.romanow.services.store.service;

import ru.romanow.services.store.model.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface StoreService {

    @Nonnull
    UserOrdersResponse findUserOrders(@Nonnull UUID userId);

    @Nonnull
    UserOrderResponse findUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId);

    @Nullable
    UUID makePurchase(@Nonnull UUID userId, @Nonnull PurchaseRequest request);

    void refundPurchase(@Nonnull UUID userId, @Nonnull UUID orderId);

    @Nonnull
    WarrantyResponse warrantyRequest(@Nonnull UUID userId, @Nonnull UUID orderId, @Nonnull WarrantyRequest request);
}
