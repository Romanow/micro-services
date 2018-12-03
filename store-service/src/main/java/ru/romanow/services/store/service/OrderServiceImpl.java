package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanow.services.payment.model.PaymentInfoResponse;
import ru.romanow.services.store.model.*;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
        return null;
    }

    @Nonnull
    @Override
    public UserOrdersResponse findUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId) {
        CompletableFuture.runAsync(() -> paymentService.getPaymentInfoByOrder(userId, orderId))
                .thenApplyAsync(this::orderDataRequest)

        CompletableFuture.allOf(paymentRequest, warrantyRequest).join();
        return new UserOrderResponse()
                .setOrderId(orderId);
    }

    private CompletableFuture<Void> orderDataRequest(@Nonnull UUID orderId, @Nonnull UUID itemId) {
        if (itemId != null) {
            CompletableFuture.runAsync(() -> warehouseService.getOrderInfo(itemId));
            CompletableFuture.runAsync(() -> warehouseService.getOrderWarranty(orderId, itemId));
            return CompletableFuture.allOf(paymentRequest, warrantyRequest).join();
        }
        return null;
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
