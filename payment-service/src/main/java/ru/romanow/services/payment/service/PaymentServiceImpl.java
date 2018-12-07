package ru.romanow.services.payment.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanow.services.payment.domain.Payment;
import ru.romanow.services.payment.model.PaymentInfoResponse;
import ru.romanow.services.payment.repository.PaymentRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PaymentServiceImpl
        implements PaymentService {
    private final PaymentRepository paymentRepository;

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public PaymentInfoResponse getUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId) {
        return paymentRepository.findByUserIdAndOrderId(userId, orderId)
                .map(this::buildPaymentInfo)
                .orElse(null);
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<PaymentInfoResponse> getUserOrders(@Nonnull UUID userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(this::buildPaymentInfo)
                .collect(toList());
    }

    @Nonnull
    private PaymentInfoResponse buildPaymentInfo(@Nonnull Payment payment) {
        return new PaymentInfoResponse()
                .setOrderId(payment.getOrderId())
                .setItemId(payment.getItemId())
                .setOrderDate(payment.getOrderDate().format(DateTimeFormatter.ISO_DATE_TIME))
                .setStatus(payment.getStatus());
    }
}
