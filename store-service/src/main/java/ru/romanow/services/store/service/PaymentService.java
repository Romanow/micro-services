package ru.romanow.services.store.service;

import ru.romanow.services.payment.model.PaymentInfoResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public interface PaymentService {

    @Nullable
    PaymentInfoResponse getPaymentInfoByOrder(@Nonnull UUID userId, @Nonnull UUID orderId);
}
