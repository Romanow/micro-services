package ru.romanow.services.payment.service;

import ru.romanow.services.payment.domain.Order;
import ru.romanow.services.payment.model.OrderInfoResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public interface PaymentService {

    @Nonnull
    Order getOrderByUid(@Nonnull UUID orderId);

    @Nullable
    OrderInfoResponse getUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId);

    @Nonnull
    List<OrderInfoResponse> getUserOrders(@Nonnull UUID userId);

    void createOrder(@Nonnull UUID orderId, @Nonnull UUID userId, @Nonnull UUID itemId);

    boolean cancelPayment(@Nonnull UUID orderId);

    boolean checkOrder(@Nonnull UUID orderId);
}
