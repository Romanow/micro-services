package ru.romanow.services.warehouse.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderInfoResponse {
    private String model;

}
