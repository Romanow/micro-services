package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.romanow.core.spring.rest.client.SpringRestClient;
import ru.romanow.services.common.config.CircuitBreakerConfiguration.Fallback;
import ru.romanow.services.warehouse.model.OrderItemInfoResponse;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl
        implements WarehouseService {
    private static final String WAREHOUSE_SERVICE = "http://warehouse-service";

    private final CircuitBreakerFactory factory;
    private final SpringRestClient restClient;
    private final Fallback fallback;

    @Nonnull
    @Override
    public Optional<OrderItemInfoResponse> getItemInfo(@Nonnull UUID itemId) {
        final String url = WAREHOUSE_SERVICE + "/api/v1/" + itemId;
        return factory
                .create("getItemInfo")
                .run(() -> restClient.get(url, OrderItemInfoResponse.class).execute(),
                     throwable -> fallback.apply(HttpMethod.GET, url, throwable));
    }

}
