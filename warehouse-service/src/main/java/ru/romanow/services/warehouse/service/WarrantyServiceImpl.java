package ru.romanow.services.warehouse.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.romanow.services.warehouse.exceptions.WarrantyProcessException;
import ru.romanow.services.warranty.modal.ItemWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarrantyServiceImpl
        implements WarrantyService {
    private static final Logger logger = LoggerFactory.getLogger(WarrantyService.class);

    private static final String WARRANTY_SERVICE = "http://warranty-service";

    private final WarehouseService warehouseService;
    private final RestTemplate restTemplate;

    @Nonnull
    @Override
    public OrderWarrantyResponse warrantyRequest(@Nonnull UUID itemId, @Nonnull OrderWarrantyRequest request) {
        logger.info("Warranty request (reason: {}) on item '{}'", request.getReason(), itemId);
        final int availableCount = warehouseService.checkItemAvailableCount(itemId);
        final ItemWarrantyRequest warrantyRequest =
                new ItemWarrantyRequest()
                        .setReason(request.getReason())
                        .setAvailableCount(availableCount);

        logger.debug("Request to WarrantyService to check warranty on item '{}'", itemId);
        return requestToWarranty(itemId, warrantyRequest)
                .orElseThrow(() -> new WarrantyProcessException("Can't process warranty request"));
    }

    @Nonnull
    @HystrixCommand
    private Optional<OrderWarrantyResponse> requestToWarranty(@Nonnull UUID itemId, @Nonnull ItemWarrantyRequest request) {
        return Optional.ofNullable(restTemplate.postForObject(WARRANTY_SERVICE + "/api/" + itemId + "/warranty", request, OrderWarrantyResponse.class));
    }
}
