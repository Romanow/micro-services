package ru.romanow.services.warehouse.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanow.services.warehouse.domain.Item;
import ru.romanow.services.warehouse.model.ItemInfoResponse;
import ru.romanow.services.warehouse.repository.WarehouseRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl
        implements WarehouseService {
    private final WarehouseRepository warehouseRepository;

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public ItemInfoResponse getItemInfo(@Nonnull UUID itemId) {
        return warehouseRepository.findItemByItemId(itemId)
                .map(this::buildItemInfo)
                .orElse(null);
    }

    @Nonnull
    private ItemInfoResponse buildItemInfo(@Nonnull Item item) {
        return new ItemInfoResponse()
                .setItemId(item.getItemId())
                .setModel(item.getModel())
                .setSize(item.getSize());
    }
}
