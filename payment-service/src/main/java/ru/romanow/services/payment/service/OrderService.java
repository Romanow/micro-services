package ru.romanow.services.payment.service;

import ru.romanow.services.payment.model.CreateOrderRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.UUID;

public interface OrderService {

    @Nonnull
    UUID makeOrder(@Nonnull UUID userId, @Valid CreateOrderRequest request);

    void refundOrder(@Nonnull UUID orderId);

    @Nonnull
    OrderWarrantyResponse checkWarranty(@Nonnull UUID orderId, @Nonnull OrderWarrantyRequest request);
}
