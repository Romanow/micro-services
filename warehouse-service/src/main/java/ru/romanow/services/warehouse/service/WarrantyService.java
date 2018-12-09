package ru.romanow.services.warehouse.service;

import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import java.util.UUID;

public interface WarrantyService {

    @Nonnull
    OrderWarrantyResponse warrantyRequest(@Nonnull UUID itemId, @Nonnull OrderWarrantyRequest request)
}
