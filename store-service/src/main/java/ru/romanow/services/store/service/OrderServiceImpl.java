package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanow.services.payment.model.PaymentInfoResponse;
import ru.romanow.services.store.model.*;
import ru.romanow.services.warehouse.model.OrderInfoResponse;
import ru.romanow.services.warranty.modal.WarrantyInfoResponse;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.CompletableFuture.supplyAsync;
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
        return null;
    }

    @Nonnull
    @Override
    public UserOrderResponse findUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId) {
        final PaymentInfoResponse paymentInfo = paymentService.getPaymentInfoByOrder(userId, orderId);

        final UserOrderResponse orderResponse =
                new UserOrderResponse()
                        .setOrderId(orderId)
                        .setDate(now());
        if (paymentInfo != null) {
            final UUID itemId = paymentInfo.getItemId();
            CompletableFuture<OrderInfoResponse> paymentInfoFuture =
                    supplyAsync(() -> warehouseService.getOrderInfo(itemId));
            CompletableFuture<WarrantyInfoResponse> warrantyInfoFuture =
                    supplyAsync(() -> warrantyService.getItemWarrantyInfo(itemId));

            CompletableFuture.allOf(paymentInfoFuture, warrantyInfoFuture).join();

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
