package ru.romanow.services.warehouse.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class OrderItemInfoResponse {
    private UUID itemId;
    private String model;
    private String size;
}
