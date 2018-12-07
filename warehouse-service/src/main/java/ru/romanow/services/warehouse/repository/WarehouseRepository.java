package ru.romanow.services.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.romanow.services.warehouse.domain.Item;

import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository
        extends JpaRepository<Item, Integer> {
    Optional<Item> findItemByItemId(UUID itemId);
}
