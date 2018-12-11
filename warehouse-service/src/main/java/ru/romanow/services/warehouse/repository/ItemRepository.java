package ru.romanow.services.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.romanow.services.warehouse.domain.Item;
import ru.romanow.services.warehouse.model.enums.SizeChart;

import java.util.Optional;
import java.util.UUID;

public interface ItemRepository
        extends JpaRepository<Item, Integer> {
    Optional<Item> findItemByModelAndSize(String model, SizeChart size);

    @Modifying
    @Query("update Item set availableCount = availableCount - 1 where id = :id")
    void takeOneItem(@Param("id") Integer id);

    @Query("update Item " +
                   "set availableCount = availableCount + 1 " +
                   "where id = (select id from OrderItem where uid = :itemId)")
    void returnOneItem(@Param("itemId") UUID itemId);
}
