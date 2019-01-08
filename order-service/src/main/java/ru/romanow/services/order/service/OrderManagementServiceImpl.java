package ru.romanow.services.order.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.romanow.services.order.domain.Order;
import ru.romanow.services.order.exceptions.CreateOrderException;
import ru.romanow.services.order.exceptions.WarrantyProcessingException;
import ru.romanow.services.order.model.CreateOrderRequest;
import ru.romanow.services.order.model.enums.SizeChart;
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
    private static final Logger logger = LoggerFactory.getLogger(OrderManagementService.class);

    private final OrderService orderService;
    private final WarehouseService warehouseService;
    private final WarrantyService warrantyService;

    @Nonnull
    @Override
    public UUID makeOrder(@Nonnull UUID userId, @Valid CreateOrderRequest request) {
        final String model = request.getModel();
        final SizeChart size = request.getSize();

        logger.info("Create order (model: {}, sie: {}) for user '{}'", model, size, userId);

        final UUID orderId = UUID.randomUUID();
        logger.debug("Request to WH to take item (model: {}, size: {}) for order '{}'", model, size, orderId);
        Optional<UUID> opt = warehouseService.takeItem(orderId, model, size);
        if (opt.isPresent()) {
            final UUID itemId = opt.get();
            logger.debug("Request to WarrantyService to start warranty on item '{}' for oder '{}'", itemId, orderId);
            warrantyService.startWarranty(itemId);
            orderService.createOrder(orderId, userId, itemId);

            return orderId;
        }

        throw new CreateOrderException("Can't create order");
    }

    @Override
    public void refundOrder(@Nonnull UUID orderId) {
        logger.info("Return order '{}'", orderId);
        final Order order = orderService.getOrderByUid(orderId);

        final UUID itemId = order.getItemId();

        logger.debug("Request to WH to return item '{}' for order '{}'", itemId, orderId);
        warehouseService.returnItem(orderId, itemId);

        logger.debug("Request to WarrantyService to stop warranty for item '{}' in order '{}'", itemId, orderId);
        warrantyService.stopWarranty(itemId);

        orderService.cancelOrder(orderId);
    }

    @Nonnull
    @Override
    public OrderWarrantyResponse checkWarranty(@Nonnull UUID orderId, @Nonnull OrderWarrantyRequest request) {
        logger.info("Check warranty (reason: {}) for order '{}'", request.getReason(), orderId);
        final Order order = orderService.getOrderByUid(orderId);

        final UUID itemId = order.getItemId();
        logger.debug("Request to WarrantyService to check warranty for item '{}' in order '{}'", itemId, orderId);
        return warehouseService
                .checkWarrantyItem(itemId, request)
                .orElseThrow(() -> new WarrantyProcessingException("Can't process warranty request"));
    }
}