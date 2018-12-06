package ru.romanow.services.store.service;

import ru.romanow.services.store.model.*;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface OrderService {

    @Nonnull
    UserOrdersResponse findUserOrders(@Nonnull UUID userId);

    @Nonnull
    UserOrderResponse findUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId);

    @Nonnull
    UUID makePurchase(@Nonnull UUID userId, @Nonnull PurchaseRequest request);

    @Nonnull
    RefundResponse refundPurchase(@Nonnull UUID userId, @Nonnull UUID orderId);

    @Nonnull
    WarrantyResponse warrantyRequest(@Nonnull UUID userId, @Nonnull UUID orderId, @Nonnull WarrantyRequest request);
}
