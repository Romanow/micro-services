package ru.romanow.services.store.service;

import ru.romanow.services.payment.model.PaymentInfoResponse;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentService {

    @Nonnull
    Optional<PaymentInfoResponse> getPaymentInfoByOrder(@Nonnull UUID userId, @Nonnull UUID orderId);

    @Nonnull
    Optional<List<PaymentInfoResponse>> getPaymentInfoByUser(@Nonnull UUID userId);
}
