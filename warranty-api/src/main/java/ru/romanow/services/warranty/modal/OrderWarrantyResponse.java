package ru.romanow.services.warranty.modal;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderWarrantyResponse {
    private String warrantyDate;
    private String decision;
}
