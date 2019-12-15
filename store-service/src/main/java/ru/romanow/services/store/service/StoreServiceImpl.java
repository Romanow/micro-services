package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.romanow.services.order.model.OrderInfoResponse;
import ru.romanow.services.order.model.OrdersInfoResponse;
import ru.romanow.services.store.exceptions.OrderProcessException;
import ru.romanow.services.store.exceptions.UserNotFoundException;
import ru.romanow.services.store.exceptions.WarrantyProcessException;
import ru.romanow.services.store.model.*;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class StoreServiceImpl
        implements StoreService {
    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

    private final UserService userService;
    private final WarehouseService warehouseService;
    private final OrderService orderService;
    private final WarrantyService warrantyService;

    @Nonnull
    @Override
    public UserOrdersResponse findUserOrders(@Nonnull UUID userId) {
        logger.info("Get user '{}' orders", userId);
        if (!userService.checkUserExists(userId)) {
            throw new UserNotFoundException(format("User '%s' not found", userId));
        }

        final List<UserOrderResponse> orders = new ArrayList<>();
        logger.debug("Request to OrderService for user '{}' orders", userId);
        final Optional<OrdersInfoResponse> userOrders =
                orderService.getOrderInfoByUser(userId);

        if (userOrders.isPresent()) {
            logger.info("User '{}' has {} orders", userId, userOrders.get().size());

            // TODO переделать на batch-операции
            for (OrderInfoResponse orderInfo : userOrders.get()) {
                logger.info("Processing user '{}' order '{}'", userId, orderInfo.getOrderId());
                final UserOrderResponse order =
                        new UserOrderResponse()
                                .setOrderId(orderInfo.getOrderId())
                                .setDate(orderInfo.getOrderDate());

                final UUID itemId = orderInfo.getItemId();
                logger.debug("Request to WH for item '{}' info by order '{}'", itemId, orderInfo.getOrderId());
                warehouseService.getItemInfo(itemId)
                                .ifPresent(info -> order
                                        .setModel(info.getModel())
                                        .setSize(info.getSize()));

                logger.debug("Request to Warranty for item '{}' info by order '{}'", itemId, orderInfo.getOrderId());
                warrantyService.getItemWarrantyInfo(itemId)
                               .ifPresent(warrantyInfo -> order
                                       .setWarrantyDate(warrantyInfo.getWarrantyDate())
                                       .setWarrantyStatus(warrantyInfo.getStatus()));

                orders.add(order);
            }
        } else {
            logger.warn("User '{}' has no orders", userId);
        }

        return new UserOrdersResponse(orders);
    }

    @Nonnull
    @Override
    public UserOrderResponse findUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId) {
        logger.info("Get info for user '{}' order '{}'", userId, orderId);
        if (!userService.checkUserExists(userId)) {
            throw new UserNotFoundException(format("User '%s' not found", userId));
        }

        logger.debug("Request to OrderService for user '{}' order '{}'", userId, orderId);
        final Optional<OrderInfoResponse> orderInfo =
                orderService.getOrderInfo(userId, orderId);
        final UserOrderResponse orderResponse =
                new UserOrderResponse().setOrderId(orderId);

        if (orderInfo.isPresent()) {
            logger.info("Processing user '{}' order '{}'", userId, orderId);

            final UUID itemId = orderInfo.get().getItemId();
            orderResponse.setDate(orderInfo.get().getOrderDate());
            logger.debug("Request to WH for item '{}' info by order '{}'", itemId, orderId);
            warehouseService.getItemInfo(itemId)
                            .ifPresent(info -> orderResponse
                                    .setModel(info.getModel())
                                    .setSize(info.getSize()));

            logger.debug("Request to WarrantyService for item '{}' info by order '{}'", itemId, orderId);
            warrantyService.getItemWarrantyInfo(itemId)
                           .ifPresent(warrantyInfo -> orderResponse
                                   .setWarrantyDate(warrantyInfo.getWarrantyDate())
                                   .setWarrantyStatus(warrantyInfo.getStatus()));
        } else {
            logger.warn("User '{}' has no order '{}'", userId, orderId);
        }

        return orderResponse;
    }

    @Nullable
    @Override
    public UUID makePurchase(@Nonnull UUID userId, @Nonnull PurchaseRequest request) {
        logger.info("Process user '{}' purchase request '{}'", userId, request);
        if (!userService.checkUserExists(userId)) {
            throw new UserNotFoundException(format("User '%s' not found", userId));
        }

        logger.info("Request to OrderService for user '{}' to process order '{}'", userId, request);
        return orderService
                .makePurchase(userId, request)
                .orElseThrow(() -> new OrderProcessException("Order not created"));
    }

    @Override
    public void refundPurchase(@Nonnull UUID userId, @Nonnull UUID orderId) {
        logger.info("Process user '{}' return request for order '{}'", userId, orderId);
        if (!userService.checkUserExists(userId)) {
            throw new UserNotFoundException(format("User '%s' not found", userId));
        }

        logger.info("Request to OrderService for user '{}' to cancel order '{}'", userId, orderId);
        orderService.refundPurchase(orderId);
    }

    @Nonnull
    @Override
    public WarrantyResponse warrantyRequest(@Nonnull UUID userId, @Nonnull UUID orderId, @Nonnull WarrantyRequest request) {
        logger.info("Process user '{}' warranty request for order '{}'", userId, orderId);
        if (!userService.checkUserExists(userId)) {
            throw new UserNotFoundException(format("User '%s' not found", userId));
        }

        logger.info("Request to OrderService for user '{}' and order '{}' to make warranty request ({})", userId, orderId, request.getReason());
        return orderService
                .warrantyRequest(orderId, request)
                .map(resp -> buildWarrantyResponse(orderId, resp))
                .orElseThrow(() -> new WarrantyProcessException("Warranty processed with exception"));
    }

    @Nonnull
    private WarrantyResponse buildWarrantyResponse(@Nonnull UUID orderId, @Nonnull OrderWarrantyResponse response) {
        return new WarrantyResponse()
                .setOrderId(orderId)
                .setDecision(response.getDecision())
                .setWarrantyDate(response.getWarrantyDate());
    }
}
