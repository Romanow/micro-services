package ru.romanow.services.warranty.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanow.services.warranty.domain.Warranty;
import ru.romanow.services.warranty.modal.WarrantyInfoResponse;
import ru.romanow.services.warranty.repository.WarrantyRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarrantyServiceImpl
        implements WarrantyService {
    private final WarrantyRepository warrantyRepository;

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public WarrantyInfoResponse getWarrantyInfo(@Nonnull UUID itemId) {
        return warrantyRepository.findWarrantyByItemId(itemId)
                .map(this::buildWarrantyInfo)
                .orElse(null);
    }

    @Nonnull
    private WarrantyInfoResponse buildWarrantyInfo(@Nonnull Warranty warranty) {
        return new WarrantyInfoResponse()
                .setItemId(warranty.getItemId())
                .setStatus(warranty.getStatus())
                .setWarrantyDate(warranty.getWarrantyDate().format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
