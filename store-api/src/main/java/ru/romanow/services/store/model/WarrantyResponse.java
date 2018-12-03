package ru.romanow.services.store.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.store.model.enums.WarrantyDecision;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class WarrantyResponse {
    private UUID orderId;
    private WarrantyDecision decision;
}
