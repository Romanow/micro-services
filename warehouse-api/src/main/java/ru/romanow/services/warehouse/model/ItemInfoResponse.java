package ru.romanow.services.warehouse.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.warehouse.model.enums.SizeChart;

@Data
@Accessors(chain = true)
public class ItemInfoResponse {
    private String model;
    private SizeChart size;
    private int availableCount;
}
