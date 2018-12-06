package ru.romanow.services.payment.model;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.payment.model.enums.PaymentStatus;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class PaymentInfoResponse {
    private UUID orderId;
    private UUID itemId;
    private PaymentStatus status;
}
