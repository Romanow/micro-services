package ru.romanow.services.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.romanow.services.payment.domain.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository
        extends JpaRepository<Order, Integer> {
    Optional<Order> findByOrderId(UUID orderId);

    Optional<Order> findByUserIdAndOrderId(UUID userId, UUID orderId);

    List<Order> findByUserId(UUID userId);

    @Modifying
    @Query("delete from Order p where p.orderId = :orderId")
    int deletePayment(@Param("orderId") UUID orderId);
}
