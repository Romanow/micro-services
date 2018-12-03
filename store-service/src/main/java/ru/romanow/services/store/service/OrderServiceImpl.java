package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanow.services.store.model.*;

import javax.annotation.Nonnull;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl
        implements OrderService {
    private final UserService userService;

    @Nonnull
    @Override
    public UserOrdersResponse findUserOrders(@Nonnull UUID userId) {
        return null;
    }

    @Nonnull
    @Override
    public UserOrdersResponse findUserOrder(@Nonnull UUID userId, @Nonnull UUID orderId) {
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
