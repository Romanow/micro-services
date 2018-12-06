package ru.romanow.services.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.romanow.services.warranty.modal.WarrantyInfoResponse;

import javax.annotation.Nonnull;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarrantyServiceImpl
        implements WarrantyService {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);
    private static final String WARRANTY_SERVICE = "warranty-service";

    private final RestTemplate restTemplate;

    @Nonnull
    @Override
    @HystrixCommand(fallbackMethod = "getItemWarrantyInfoFallback")
    public WarrantyInfoResponse getItemWarrantyInfo(@Nonnull UUID itemId) {
        return restTemplate.getForObject(WARRANTY_SERVICE + "/api/" + itemId, WarrantyInfoResponse.class);
    }

    public WarrantyInfoResponse getItemWarrantyInfoFallback(@Nonnull UUID itemId) {
        logger.warn("Request to '%s/api/%s/%s failed. Use fallback", WARRANTY_SERVICE, itemId);
        return new WarrantyInfoResponse().setItemId(itemId);
    }
}
