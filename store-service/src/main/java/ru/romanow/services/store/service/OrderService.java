package ru.romanow.services.store.service;

import ru.romanow.services.order.model.OrderInfoResponse;
import ru.romanow.services.order.model.OrdersInfoResponse;
import ru.romanow.services.store.model.PurchaseRequest;
import ru.romanow.services.store.model.WarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    @Nonnull
    Optional<OrderInfoResponse> getOrderInfo(@Nonnull UUID userId, @Nonnull UUID orderId);

    @Nonnull
    Optional<OrdersInfoResponse> getOrderInfoByUser(@Nonnull UUID userId);

    @Nonnull
    Optional<UUID> makePurchase(@Nonnull UUID userId, @Nonnull PurchaseRequest request);

    void refundPurchase(@Nonnull UUID orderId);

    @Nonnull
    Optional<OrderWarrantyResponse> warrantyRequest(@Nonnull UUID orderId, @Nonnull WarrantyRequest request);
}
