package ru.romanow.services.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.romanow.services.warehouse.model.enums.SizeChart;

import java.util.UUID;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoResponse {
    private UUID itemId;
    private String model;
    private SizeChart size;
}
