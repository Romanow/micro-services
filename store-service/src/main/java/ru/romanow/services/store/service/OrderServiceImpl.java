package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanow.services.payment.model.OrderInfoResponse;
import ru.romanow.services.store.exceptions.OrderProcessException;
import ru.romanow.services.store.exceptions.UserNotFoundException;
import ru.romanow.services.store.model.*;
import ru.romanow.services.store.model.enums.SizeChart;
import ru.romanow.services.store.model.enums.WarrantyDecision;
import ru.romanow.services.store.model.enums.WarrantyStatus;
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
public class OrderServiceImpl
        implements OrderService {
    private final UserService userService;
    private final WarehouseService warehouseService;
    private final PaymentService paymentService;
    private final WarrantyService warrantyService;

    @Nonnull
    @Override
    public UserOrdersResponse findUserOrders(@Nonnull UUID userId) {
        if (!userService.checkUserExists(userId)) {
            throw new UserNotFoundException(format("User '%s' not found", userId));
        }
        final List<UserOrderResponse> orders = new ArrayList<>();
        final Optional<List<OrderInfoResponse>> userOrders = paymentService.getOrderInfoByUser(userId);
        if (userOrders.isPresent()) {
            for (OrderInfoResponse paymentInfo : userOrders.get()) {
                final UserOrderResponse order =
                        new UserOrderResponse()
                                .setOrderId(paymentInfo.getOrderId())
                                .setDate(paymentInfo.getOrderDate());

                final UUID itemId = paymentInfo.getItemId();
                warehouseService.getItemInfo(itemId)
                                .map(orderInfo -> order
                                        .setModel(orderInfo.getModel())
                                        .setSize(convertToStoreSize(orderInfo.getSize())));

                warrantyService.getItemWarrantyInfo(itemId)
                               .map(warrantyInfo -> order
                                       .setWarrantyDate(warrantyInfo.getWarrantyDate())
                                       .setWarrantyStatus(convertToStoreWarrantyStatus(warrantyInfo.getStatus())));

                orders.add(order);
            }
        }

        return new UserOrdersResponse(orders);
    }

    @Nonnull
    @Override
    public UserOrderResponse findUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId) {
        if (!userService.checkUserExists(userId)) {
            throw new UserNotFoundException(format("User '%s' not found", userId));
        }

        final Optional<OrderInfoResponse> orderInfo = paymentService.getOrderInfo(userId, orderId);

        final UserOrderResponse orderResponse =
                new UserOrderResponse().setOrderId(orderId);
        if (orderInfo.isPresent()) {
            final UUID itemId = orderInfo.get().getItemId();
            orderResponse.setDate(orderInfo.get().getOrderDate());
            warehouseService.getItemInfo(itemId)
                            .map(info -> orderResponse
                                    .setModel(info.getModel())
                                    .setSize(convertToStoreSize(info.getSize())));

            warrantyService.getItemWarrantyInfo(itemId)
                           .map(warrantyInfo -> orderResponse
                                   .setWarrantyDate(warrantyInfo.getWarrantyDate())
                                   .setWarrantyStatus(convertToStoreWarrantyStatus(warrantyInfo.getStatus())));
        }

        return orderResponse;
    }

    @Nullable
    @Override
    public UUID makePurchase(@Nonnull UUID userId, @Nonnull PurchaseRequest request) {
        if (!userService.checkUserExists(userId)) {
            throw new UserNotFoundException(format("User '%s' not found", userId));
        }

        return paymentService
                .makePurchase(userId, request)
                .orElseThrow(() -> new OrderProcessException("Order not created"));
    }

    @Override
    public void refundPurchase(@Nonnull UUID userId, @Nonnull UUID orderId) {
        if (!userService.checkUserExists(userId)) {
            throw new UserNotFoundException(format("User '%s' not found", userId));
        }

        paymentService.refundPurchase(orderId);
    }

    @Nonnull
    @Override
    public WarrantyResponse warrantyRequest(@Nonnull UUID userId, @Nonnull UUID orderId, @Nonnull WarrantyRequest request) {
        if (!userService.checkUserExists(userId)) {
            throw new UserNotFoundException(format("User '%s' not found", userId));
        }

        return paymentService
                .warrantyRequest(orderId, request)
                .map(resp -> buildWarrantyResponse(orderId, resp))
                .orElseThrow(() -> new WarrantyProcessException("Warranty processed with exception"));
    }

    @Nonnull
    private WarrantyResponse buildWarrantyResponse(@Nonnull UUID orderId, @Nonnull OrderWarrantyResponse response) {
        return new WarrantyResponse()
                .setOrderId(orderId)
                .setDecision(convertToStoreWarrantyDecision(response.getDecision()))
                .setWarrantyDate(response.getWarrantyDate());
    }

    @Nonnull
    private WarrantyDecision convertToStoreWarrantyDecision(@Nonnull ru.romanow.services.warranty.modal.enums.WarrantyDecision decision) {
        return WarrantyDecision.valueOf(decision.name());
    }

    @Nonnull
    private WarrantyStatus convertToStoreWarrantyStatus(@Nonnull ru.romanow.services.warranty.modal.enums.WarrantyStatus status) {
        return WarrantyStatus.valueOf(status.name());
    }

    @Nonnull
    private SizeChart convertToStoreSize(@Nonnull ru.romanow.services.warehouse.model.enums.SizeChart size) {
        return SizeChart.valueOf(size.name());
    }
}
