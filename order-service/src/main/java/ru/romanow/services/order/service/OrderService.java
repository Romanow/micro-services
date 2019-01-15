package ru.romanow.services.order.service;

import ru.romanow.services.order.domain.Order;
import ru.romanow.services.order.model.OrderInfoResponse;
import ru.romanow.services.order.model.OrdersInfoResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface OrderService {

    @Nonnull
    Order getOrderByUid(@Nonnull UUID orderId);

    @Nullable
    OrderInfoResponse getUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId);

    @Nonnull
    OrdersInfoResponse getUserOrders(@Nonnull UUID userId);

    void createOrder(@Nonnull UUID orderId, @Nonnull UUID userId, @Nonnull UUID itemId);

    boolean cancelOrder(@Nonnull UUID orderId);

    boolean checkOrder(@Nonnull UUID orderId);
}
