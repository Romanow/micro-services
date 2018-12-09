package ru.romanow.services.warehouse.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.warehouse.model.enums.SizeChart;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class OrderItemRequest {

    @NotEmpty(message = "{field.is.empty")
    private UUID orderId;

    @NotEmpty(message = "{field.is.empty")
    private String model;

    @NotNull(message = "{field.is.null")
    private SizeChart size;
}
