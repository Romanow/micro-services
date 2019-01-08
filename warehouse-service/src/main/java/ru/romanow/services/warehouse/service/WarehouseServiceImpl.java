package ru.romanow.services.warehouse.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanow.services.warehouse.domain.Item;
import ru.romanow.services.warehouse.domain.OrderItem;
import ru.romanow.services.warehouse.exceptions.ItemNotAvailableException;
import ru.romanow.services.warehouse.model.OrderItemInfoResponse;
import ru.romanow.services.warehouse.model.OrderItemRequest;
import ru.romanow.services.warehouse.model.enums.SizeChart;
import ru.romanow.services.warehouse.repository.ItemRepository;
import ru.romanow.services.warehouse.repository.OrderItemRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl
        implements WarehouseService {
    private final static Logger logger = LoggerFactory.getLogger(WarrantyService.class);

    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public OrderItemInfoResponse getItemInfo(@Nonnull UUID itemId) {
        logger.info("Get info for item '{}'", itemId);
        return orderItemRepository
                .findItemByUid(itemId)
                .map(orderItem -> buildOrderItemInfo(itemId, orderItem.getItem()))
                .orElse(null);
    }

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public OrderItem getOrderItem(@Nonnull UUID itemId) {
        return orderItemRepository
                .findItemByUid(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item '" + itemId + "' not found"));
    }

    @Nonnull
    @Override
    @Transactional
    public UUID takeItem(@Nonnull OrderItemRequest request) {
        final String model = request.getModel();
        final SizeChart size = request.getSize();
        final UUID orderId = request.getOrderId();

        logger.info("Take item (model: {}, sie: {}) for order '{}'", model, size, orderId);

        final Optional<Item> opt =
                itemRepository.findItemByModelAndSize(model, size);
        if (opt.isPresent()) {
            final Item item = opt.get();
            if (item.getAvailableCount() < 1) {
                throw new ItemNotAvailableException(format("Item '%s' is finished on warehouse", item));
            }

            final UUID uid = UUID.randomUUID();
            OrderItem orderItem =
                    new OrderItem()
                            .setItem(opt.get())
                            .setOrderId(orderId)
                            .setUid(uid);

            orderItem = orderItemRepository.save(orderItem);
            if (logger.isDebugEnabled()) {
                logger.debug("Create OrderItem '{}' for order '{}'", orderItem, orderId);
            }

            itemRepository.takeOneItem(item.getId());
            logger.debug("Take one item for itemId '{}'", item.getId());

            return uid;
        } else {
            logger.warn("No items found for model: {} and size: {}", model, size);
        }

        throw new EntityNotFoundException(
                format("Item with model '%s' and size '%s' not found", model, size));
    }

    @Override
    @Transactional
    public void returnItem(@Nonnull UUID itemId) {
        logger.info("Return one item '{}' to warehouse", itemId);
        itemRepository.returnOneItem(itemId);
        orderItemRepository.returnOrderItem(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public int checkItemAvailableCount(@Nonnull UUID itemId) {
        logger.debug("Check item '{}' to exists warehouse", itemId);
        final OrderItem orderItem = getOrderItem(itemId);
        return orderItem.getItem().getAvailableCount();
    }

    @Nonnull
    private OrderItemInfoResponse buildOrderItemInfo(@Nonnull UUID uid, @Nonnull Item item) {
        return new OrderItemInfoResponse()
                .setItemId(uid)
                .setModel(item.getModel())
                .setSize(item.getSize());
    }
}
