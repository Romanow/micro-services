package ru.romanow.services.store.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class WarrantyResponse {
    private UUID orderId;
    private String warrantyDate;
    private String decision;
}
