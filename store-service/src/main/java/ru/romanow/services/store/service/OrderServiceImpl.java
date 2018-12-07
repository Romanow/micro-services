package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanow.services.payment.model.PaymentInfoResponse;
import ru.romanow.services.store.model.*;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static ru.romanow.services.store.service.DateTimeHelper.now;

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
        final Optional<List<PaymentInfoResponse>> userOrders = paymentService.getPaymentInfoByUser(userId);
        if (userOrders.isPresent()) {
            for (PaymentInfoResponse paymentInfo: userOrders.get()) {
                final UserOrderResponse order =
                        new UserOrderResponse()
                                .setOrderId(paymentInfo.getOrderId())
                                .setDate(paymentInfo.getOrderDate());

                final UUID itemId = paymentInfo.getItemId();
                warehouseService.getOrderInfo(itemId)
                        .map(orderInfo -> order
                                .setModel(orderInfo.getModel())
                                .setSize(orderInfo.getSize()));

                warrantyService.getItemWarrantyInfo(itemId)
                        .map(warrantyInfo -> order.setStatus(warrantyInfo.getStatus()));

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

        final Optional<PaymentInfoResponse> paymentInfo = paymentService.getPaymentInfoByOrder(userId, orderId);

        final UserOrderResponse orderResponse =
                new UserOrderResponse().setOrderId(orderId);
        if (paymentInfo.isPresent()) {
            final UUID itemId = paymentInfo.get().getItemId();
            orderResponse.setDate(paymentInfo.get().getOrderDate());
            warehouseService.getOrderInfo(itemId)
                    .map(orderInfo -> orderResponse
                            .setModel(orderInfo.getModel())
                            .setSize(orderInfo.getSize()));

            warrantyService.getItemWarrantyInfo(itemId)
                    .map(warrantyInfo -> orderResponse.setStatus(warrantyInfo.getStatus()));
        }

        return orderResponse;
    }

    @Nonnull
    @Override
    public UUID makePurchase(@Nonnull UUID userId, @Nonnull PurchaseRequest request) {
        return null;
    }

    @Nonnull
    @Override
    public RefundResponse refundPurchase(@Nonnull UUID userId, @Nonnull UUID orderId) {
        return null;
    }

    @Nonnull
    @Override
    public WarrantyResponse warrantyRequest(@Nonnull UUID userId, @Nonnull UUID orderId, @Nonnull WarrantyRequest request) {
        return null;
    }
}
