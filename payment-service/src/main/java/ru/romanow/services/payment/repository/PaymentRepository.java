package ru.romanow.services.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.romanow.services.payment.domain.Payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository
        extends JpaRepository<Payment, Integer> {
    List<Payment> findByUserId(UUID userId);

    Optional<Payment> findByUserIdAndOrderId(UUID userId, UUID orderId);
}
