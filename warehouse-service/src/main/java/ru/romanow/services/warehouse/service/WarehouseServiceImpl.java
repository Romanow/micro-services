package ru.romanow.services.warehouse.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanow.services.warehouse.domain.Item;
import ru.romanow.services.warehouse.domain.OrderItem;
import ru.romanow.services.warehouse.exceptions.ItemNotAvailableException;
import ru.romanow.services.warehouse.model.ItemInfoResponse;
import ru.romanow.services.warehouse.model.OrderItemInfoResponse;
import ru.romanow.services.warehouse.model.OrderItemRequest;
import ru.romanow.services.warehouse.repository.ItemRepository;
import ru.romanow.services.warehouse.repository.OrderItemRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl
        implements WarehouseService {
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public OrderItemInfoResponse getItemInfo(@Nonnull UUID itemId) {
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
    @Transactional(readOnly = true)
    public List<ItemInfoResponse> getItemsInfo() {
        return itemRepository
                .findAll()
                .stream()
                .map(this::buildItemInfo)
                .collect(toList());
    }

    @Nonnull
    @Override
    @Transactional
    public UUID takeItem(@Nonnull OrderItemRequest request) {
        final Optional<Item> opt =
                itemRepository.findItemByModelAndSize(request.getModel(), request.getSize());
        if (opt.isPresent()) {
            final Item item = opt.get();
            if (item.getAvailableCount() < 1) {
                throw new ItemNotAvailableException(format("Item '%s' don't exists on warehouse", item));
            }

            final UUID uid = UUID.randomUUID();
            OrderItem orderItem =
                    new OrderItem()
                            .setItem(opt.get())
                            .setOrderId(request.getOrderId())
                            .setUid(uid);
            orderItemRepository.save(orderItem);
            itemRepository.takeOneItem(item.getId());

            return uid;
        }

        throw new EntityNotFoundException(
                format("Item with model '%s' and size '%s' not found", request.getModel(), request.getSize()));
    }

    @Override
    @Transactional
    public void returnItem(@Nonnull UUID itemId) {
        itemRepository.returnOneItem(itemId);
        orderItemRepository.returnOrderItem(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public int checkItemAvailableCount(@Nonnull UUID itemId) {
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

    @Nonnull
    private ItemInfoResponse buildItemInfo(@Nonnull Item item) {
        return new ItemInfoResponse()
                .setModel(item.getModel())
                .setSize(item.getSize())
                .setAvailableCount(item.getAvailableCount());
    }
}
