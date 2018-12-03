package ru.romanow.services.store.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class PurchaseRequest {

    @NotEmpty(message = "{field.is.empty")
    private String model;

    @NotNull(message = "{field.is.null")
    private String size;
}
