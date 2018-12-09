package ru.romanow.services.warranty.modal;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class ItemWarrantyRequest {
    private String reason;

    @NotNull(message = "{field.is.null")
    private Integer availableCount;
}
