package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
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

    private final CircuitBreakerFactory factory;
    private final SpringRestClient restClient;

    @Nonnull
    @Override
    public Optional<WarrantyInfoResponse> getItemWarrantyInfo(@Nonnull UUID itemId) {
        return factory.create("getItemWarrantyInfo")
                      .run(() -> restClient.get(WARRANTY_SERVICE + "/api/v1/" + itemId, WarrantyInfoResponse.class).execute(),
                           throwable -> getItemWarrantyInfoFallback(itemId, throwable));
    }

    private Optional<WarrantyInfoResponse> getItemWarrantyInfoFallback(@Nonnull UUID itemId, Throwable throwable) {
        logger.warn("Request to '{}/api/v1/{}' failed with exception: {}. Use fallback", WARRANTY_SERVICE, itemId, throwable.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("", throwable);
        }
        return Optional.empty();
    }
}
