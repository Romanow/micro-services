package ru.romanow.services.order.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanow.services.order.domain.Order;
import ru.romanow.services.order.model.OrderInfoResponse;
import ru.romanow.services.order.model.OrdersInfoResponse;
import ru.romanow.services.order.model.enums.PaymentStatus;
import ru.romanow.services.order.repository.OrderRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.UUID;

import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;
import static java.util.stream.Collectors.toCollection;

@Service
@AllArgsConstructor
public class OrderServiceImpl
        implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public Order getOrderByUid(@Nonnull UUID orderId) {
        return orderRepository
                .findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order '" + orderId + "' not found"));
    }

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public OrderInfoResponse getUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId) {
        logger.info("Get info for user '{}' order '{}'", userId, orderId);
        return orderRepository
                .findByUserIdAndOrderId(userId, orderId)
                .map(this::buildOrderInfo)
                .orElse(null);
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public OrdersInfoResponse getUserOrders(@Nonnull UUID userId) {
        logger.info("Get info for user '{}' orders", userId);
        return orderRepository
                .findByUserId(userId)
                .stream()
                .map(this::buildOrderInfo)
                .collect(toCollection(OrdersInfoResponse::new));
    }

    @Override
    @Transactional
    public void createOrder(@Nonnull UUID orderId, @Nonnull UUID userId, @Nonnull UUID itemId) {
        final Order order = new Order()
                .setUserId(userId)
                .setOrderId(orderId)
                .setOrderDate(new Date())
                .setStatus(PaymentStatus.PAID)
                .setItemId(itemId);

        orderRepository.save(order);
        if (logger.isDebugEnabled()) {
            logger.debug("Create order '{}' for user '{}' and item '{}'", orderId, userId, itemId);
        }
    }

    @Override
    @Transactional
    public boolean cancelOrder(@Nonnull UUID orderId) {
        int deleted = orderRepository.deleteOrder(orderId);
        if (logger.isDebugEnabled()) {
            logger.debug("Deleted {} order", deleted);
        }
        return deleted > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkOrder(@Nonnull UUID orderId) {
        return orderRepository.findByOrderId(orderId).isPresent();
    }

    @Nonnull
    private OrderInfoResponse buildOrderInfo(@Nonnull Order order) {
        return new OrderInfoResponse()
                .setOrderId(order.getOrderId())
                .setItemId(order.getItemId())
                .setOrderDate(formatDate(order.getOrderDate()))
                .setStatus(order.getStatus());
    }

    @Nonnull
    private String formatDate(@Nonnull Date warrantyDate) {
        return ofInstant(warrantyDate.toInstant(), systemDefault()).format(ISO_DATE_TIME);
    }
}
