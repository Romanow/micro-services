package ru.romanow.services.payment.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanow.services.payment.domain.Order;
import ru.romanow.services.payment.model.OrderInfoResponse;
import ru.romanow.services.payment.model.enums.PaymentStatus;
import ru.romanow.services.payment.repository.PaymentRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class PaymentServiceImpl
        implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public Order getOrderByUid(@Nonnull UUID orderId) {
        return paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order '" + orderId + "' not found"));
    }

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public OrderInfoResponse getUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId) {
        return paymentRepository.findByUserIdAndOrderId(userId, orderId)
                .map(this::buildPaymentInfo)
                .orElse(null);
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public List<OrderInfoResponse> getUserOrders(@Nonnull UUID userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(this::buildPaymentInfo)
                .collect(toList());
    }

    @Override
    @Transactional
    public void createOrder(@Nonnull UUID orderId, @Nonnull UUID userId, @Nonnull UUID itemId) {
        final Order order = new Order()
                .setUserId(userId)
                .setOrderId(orderId)
                .setOrderDate(now())
                .setStatus(PaymentStatus.PAID)
                .setItemId(itemId);

        paymentRepository.save(order);
        if (logger.isDebugEnabled()) {
            logger.debug("Create order '{}' for user '{}' and item '{}'", orderId, userId, itemId);
        }
    }

    @Override
    @Transactional
    public boolean cancelPayment(@Nonnull UUID orderId) {
        int deleted = paymentRepository.deletePayment(orderId);
        if (logger.isDebugEnabled()) {
            logger.debug("Deleted {} payments", deleted);
        }
        return deleted > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkOrder(@Nonnull UUID orderId) {
        return paymentRepository.findByOrderId(orderId).isPresent();
    }

    @Nonnull
    private OrderInfoResponse buildPaymentInfo(@Nonnull Order order) {
        return new OrderInfoResponse()
                .setOrderId(order.getOrderId())
                .setItemId(order.getItemId())
                .setOrderDate(order.getOrderDate().format(DateTimeFormatter.ISO_DATE_TIME))
                .setStatus(order.getStatus());
    }
}
