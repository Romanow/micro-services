package ru.romanow.services.warranty.modal;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.warranty.modal.enums.WarrantyDecision;

@Data
@Accessors(chain = true)
public class OrderWarrantyResponse {
    private String warrantyDate;
    private WarrantyDecision decision;
}
