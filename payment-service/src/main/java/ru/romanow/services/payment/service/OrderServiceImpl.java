package ru.romanow.services.payment.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanow.services.payment.domain.Order;
import ru.romanow.services.payment.exceptions.CreateOrderException;
import ru.romanow.services.payment.exceptions.WarrantyProcessingException;
import ru.romanow.services.payment.model.CreateOrderRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl
        implements OrderService {
    private final PaymentService paymentService;
    private final WarehouseService warehouseService;
    private final WarrantyService warrantyService;

    @Nonnull
    @Override
    public UUID makeOrder(@Nonnull UUID userId, @Valid CreateOrderRequest request) {
        final UUID orderId = UUID.randomUUID();
        Optional<UUID> opt = warehouseService.takeItem(orderId, request.getModel(), request.getSize());
        if (opt.isPresent()) {
            final UUID itemId = opt.get();
            warrantyService.startWarranty(itemId);
            paymentService.createOrder(orderId, userId, itemId);

            return orderId;
        }

        throw new CreateOrderException("Can't create order");
    }

    @Override
    public void refundOrder(@Nonnull UUID orderId) {
        final Order order = paymentService.getOrderByUid(orderId);
        warehouseService.returnItem(orderId, order.getItemId());
        warrantyService.stopWarranty(order.getItemId());
        paymentService.cancelPayment(orderId);
    }

    @Nonnull
    @Override
    public OrderWarrantyResponse checkWarranty(@Nonnull UUID orderId, @Nonnull OrderWarrantyRequest request) {
        final Order order = paymentService.getOrderByUid(orderId);
        return warehouseService
                .checkWarrantyItem(order.getItemId(), request)
                .orElseThrow(() -> new WarrantyProcessingException("Can't process warranty request"));
    }
}