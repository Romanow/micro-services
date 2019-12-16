package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import ru.romanow.core.spring.rest.client.SpringRestClient;
import ru.romanow.services.warehouse.model.OrderItemInfoResponse;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl
        implements WarehouseService {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);
    private static final String WAREHOUSE_SERVICE = "http://warehouse-service";

    private final CircuitBreakerFactory factory;
    private final SpringRestClient restClient;

    @Nonnull
    @Override
    public Optional<OrderItemInfoResponse> getItemInfo(@Nonnull UUID itemId) {
        return factory
                .create("getItemInfo")
                .run(() -> restClient
                             .get(WAREHOUSE_SERVICE + "/api/v1/" + itemId, OrderItemInfoResponse.class)
                             .execute(),
                     throwable -> getOrderInfoFallback(itemId, throwable));
    }

    private Optional<OrderItemInfoResponse> getOrderInfoFallback(@Nonnull UUID itemId, Throwable throwable) {
        logger.warn("Request to '{}/api/v1/{}' failed with exception: {}. Use apply", WAREHOUSE_SERVICE, itemId, throwable.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("", throwable);
        }
        return Optional.empty();
    }
}
