package ru.romanow.services.store.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class UserOrderResponse {
    private UUID orderId;
    private String date;
    private String model;
    private String size;
    private String warrantyDate;
    private String warrantyStatus;
}
