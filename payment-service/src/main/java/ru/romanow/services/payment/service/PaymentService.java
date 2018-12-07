package ru.romanow.services.payment.service;

import ru.romanow.services.payment.model.PaymentInfoResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public interface PaymentService {

    @Nullable
    PaymentInfoResponse getUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId);

    @Nonnull
    List<PaymentInfoResponse> getUserOrders(@Nonnull UUID userId);
}
