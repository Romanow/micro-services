package ru.romanow.services.store.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.store.model.enums.SizeChart;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class PurchaseRequest {

    @NotEmpty(message = "{field.is.empty")
    private String model;

    @NotNull(message = "{field.is.null")
    private SizeChart size;
}
