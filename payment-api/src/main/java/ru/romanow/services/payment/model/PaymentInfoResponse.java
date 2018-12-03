package ru.romanow.services.payment.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class PaymentInfoResponse {
    private UUID itemId;
}
