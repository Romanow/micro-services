package ru.romanow.services.warranty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.romanow.services.warranty.domain.Warranty;
import ru.romanow.services.warranty.modal.WarrantyInfoResponse;

import java.util.Optional;
import java.util.UUID;

public interface WarrantyRepository
        extends JpaRepository<Warranty, Integer> {
    Optional<Warranty> findWarrantyByItemId(UUID itemId);
}
