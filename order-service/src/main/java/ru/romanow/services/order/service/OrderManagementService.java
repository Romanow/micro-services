package ru.romanow.services.order.service;

import ru.romanow.services.order.model.CreateOrderRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.UUID;

public interface OrderManagementService {

    @Nonnull
    UUID makeOrder(@Nonnull UUID userId, @Valid CreateOrderRequest request);

    void refundOrder(@Nonnull UUID orderId);

    @Nonnull
    OrderWarrantyResponse useWarranty(@Nonnull UUID orderId, @Nonnull OrderWarrantyRequest request);
}
