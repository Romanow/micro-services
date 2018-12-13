package ru.romanow.services.store.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.store.model.enums.SizeChart;
import ru.romanow.services.store.model.enums.WarrantyStatus;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class UserOrderResponse {
    private UUID orderId;
    private String date;
    private String model;
    private SizeChart size;
    private String warrantyDate;
    private WarrantyStatus warrantyStatus;
}
