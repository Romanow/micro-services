package ru.romanow.services.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.romanow.services.warehouse.domain.OrderItem;

import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepository
        extends JpaRepository<OrderItem, Integer> {
    Optional<OrderItem> findItemByUid(UUID uid);

    @Modifying
    @Query("delete from OrderItem where uid = :itemId")
    void returnOrderItem(@Param("itemId") UUID itemId);
}
