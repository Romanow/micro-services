package ru.romanow.services.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.romanow.core.spring.rest.client.SpringRestClient;
import ru.romanow.services.warranty.modal.WarrantyInfoResponse;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarrantyServiceImpl
        implements WarrantyService {
    private static final Logger logger = LoggerFactory.getLogger(WarrantyService.class);
    private static final String WARRANTY_SERVICE = "http://warranty-service";
    private final SpringRestClient restClient;

    @Nonnull
    @Override
    @HystrixCommand(fallbackMethod = "getItemWarrantyInfoFallback")
    public Optional<WarrantyInfoResponse> getItemWarrantyInfo(@Nonnull UUID itemId) {
        return restClient.get(WARRANTY_SERVICE + "/api/" + itemId, WarrantyInfoResponse.class).execute();
    }

    public Optional<WarrantyInfoResponse> getItemWarrantyInfoFallback(@Nonnull UUID itemId, Throwable throwable) {
        logger.warn("Request to '{}/api/{}' failed with exception: {}. Use fallback", WARRANTY_SERVICE, itemId, throwable.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("", throwable);
        }
        return Optional.empty();
    }
}
