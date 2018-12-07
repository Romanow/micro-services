package ru.romanow.services.warranty.modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.romanow.services.warranty.modal.enums.WarrantyStatus;

import java.util.UUID;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class WarrantyInfoResponse {
    private UUID itemId;
    private String warrantyDate;
    private WarrantyStatus status;
}
