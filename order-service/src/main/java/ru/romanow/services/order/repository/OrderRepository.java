package ru.romanow.services.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.romanow.services.order.domain.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository
        extends JpaRepository<Order, Integer> {
    Optional<Order> findByOrderId(UUID orderId);

    Optional<Order> findByUserIdAndOrderId(UUID userId, UUID orderId);

    List<Order> findByUserId(UUID userId);

    @Modifying
    @Query("delete from Order p where p.orderId = :orderId")
    int deleteOrder(@Param("orderId") UUID orderId);
}
