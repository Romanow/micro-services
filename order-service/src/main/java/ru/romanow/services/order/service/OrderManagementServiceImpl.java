package ru.romanow.services.order.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanow.services.order.domain.Order;
import ru.romanow.services.order.exceptions.CreateOrderException;
import ru.romanow.services.order.exceptions.WarrantyProcessingException;
import ru.romanow.services.order.model.CreateOrderRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderManagementServiceImpl
        implements OrderManagementService {
    private final OrderService orderService;
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
            orderService.createOrder(orderId, userId, itemId);

            return orderId;
        }

        throw new CreateOrderException("Can't create order");
    }

    @Override
    public void refundOrder(@Nonnull UUID orderId) {
        final Order order = orderService.getOrderByUid(orderId);
        warehouseService.returnItem(orderId, order.getItemId());
        warrantyService.stopWarranty(order.getItemId());
        orderService.cancelOrder(orderId);
    }

    @Nonnull
    @Override
    public OrderWarrantyResponse checkWarranty(@Nonnull UUID orderId, @Nonnull OrderWarrantyRequest request) {
        final Order order = orderService.getOrderByUid(orderId);
        return warehouseService
                .checkWarrantyItem(order.getItemId(), request)
                .orElseThrow(() -> new WarrantyProcessingException("Can't process warranty request"));
    }
}