package ru.romanow.services.warranty.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.romanow.services.warranty.domain.Warranty;
import ru.romanow.services.warranty.modal.ItemWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;
import ru.romanow.services.warranty.modal.WarrantyInfoResponse;
import ru.romanow.services.warranty.modal.enums.WarrantyDecision;
import ru.romanow.services.warranty.modal.enums.WarrantyStatus;
import ru.romanow.services.warranty.repository.WarrantyRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityNotFoundException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class WarrantyServiceImpl
        implements WarrantyService {
    private static final Logger logger = LoggerFactory.getLogger(WarrantyService.class);

    private final WarrantyRepository warrantyRepository;

    @Nonnull
    @Override
    @Transactional(readOnly = true)
    public Warranty getWarrantyByItemId(@Nonnull UUID itemId) {
        return warrantyRepository.findWarrantyByItemId(itemId)
                                 .orElseThrow(() -> new EntityNotFoundException("Warranty not found for itemId '" + itemId + "'"));
    }

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public WarrantyInfoResponse getWarrantyInfo(@Nonnull UUID itemId) {
        return warrantyRepository
                .findWarrantyByItemId(itemId)
                .map(this::buildWarrantyInfo)
                .orElse(null);
    }

    @Nonnull
    @Override
    @Transactional
    public OrderWarrantyResponse warrantyRequest(@Nonnull UUID itemId, @Nonnull ItemWarrantyRequest request) {
        final Warranty warranty = getWarrantyByItemId(itemId);

        WarrantyDecision decision = WarrantyDecision.REFUSE;
        if (isActiveWarranty(warranty) && warranty.getStatus() == WarrantyStatus.ON_WARRANTY) {
            decision = request.getAvailableCount() > 0 ? WarrantyDecision.RETURN : WarrantyDecision.FIXING;
        }
        updateWarranty(warranty, decision, request.getReason());

        return new OrderWarrantyResponse()
                .setDecision(decision)
                .setWarrantyDate(warranty.getWarrantyDate().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    @Override
    @Transactional
    public void startWarranty(@Nonnull UUID itemId) {
        Warranty warranty =
                new Warranty()
                        .setWarrantyDate(now())
                        .setItemId(itemId)
                        .setStatus(WarrantyStatus.ON_WARRANTY);

        warranty = warrantyRepository.save(warranty);
        if (logger.isDebugEnabled()) {
            logger.debug("Create warranty {} for itemId '{}'", warranty, itemId);
        }
    }

    @Override
    @Transactional
    public void stopWarranty(@Nonnull UUID itemId) {
        final int deleted = warrantyRepository.stopWarranty(itemId);
        if (logger.isDebugEnabled()) {
            logger.debug("Deleted {} warranty", deleted);
        }
    }

    private void updateWarranty(@Nonnull Warranty warranty, @Nonnull WarrantyDecision decision, @Nullable String reason) {
        warranty.setComment(reason);
        final WarrantyStatus status = decision == WarrantyDecision.REFUSE
                                      ? WarrantyStatus.REMOVED_FROM_WARRANTY
                                      : WarrantyStatus.USE_WARRANTY;
        warranty.setStatus(status);
        warranty = warrantyRepository.save(warranty);

        if (logger.isDebugEnabled()) {
            logger.debug("Update warranty {} for itemId '{}'", warranty, warranty.getItemId());
        }
    }

    private boolean isActiveWarranty(@Nonnull Warranty warranty) {
        return warranty.getWarrantyDate().isAfter(now().minus(1, ChronoUnit.MONTHS));
    }

    @Nonnull
    private WarrantyInfoResponse buildWarrantyInfo(@Nonnull Warranty warranty) {
        return new WarrantyInfoResponse()
                .setItemId(warranty.getItemId())
                .setStatus(warranty.getStatus())
                .setWarrantyDate(warranty.getWarrantyDate().format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
