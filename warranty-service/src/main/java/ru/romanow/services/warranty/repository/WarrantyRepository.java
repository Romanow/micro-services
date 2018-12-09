package ru.romanow.services.warranty.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.romanow.services.warranty.domain.Warranty;

import java.util.Optional;
import java.util.UUID;

public interface WarrantyRepository
        extends JpaRepository<Warranty, Integer> {
    Optional<Warranty> findWarrantyByItemId(UUID itemId);

    @Modifying
    @Query("delete from Warranty where itemId = :itemId")
    int stopWarranty(@Param("itemId") UUID itemId);
}
