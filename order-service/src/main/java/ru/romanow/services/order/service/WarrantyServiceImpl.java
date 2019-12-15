package ru.romanow.services.order.service;

import lombok.AllArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import ru.romanow.core.spring.rest.client.SpringRestClient;

import javax.annotation.Nonnull;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarrantyServiceImpl
        implements WarrantyService {
    private static final String WARRANTY_SERVICE = "http://warranty-service";

    private final CircuitBreakerFactory factory;
    private final SpringRestClient restClient;

    @Override
    public void startWarranty(@Nonnull UUID itemId) {
        factory.create("startWarranty").run(() -> restClient.post(WARRANTY_SERVICE + "/api/v1/" + itemId, null, Void.class).execute());
    }

    @Override
    public void stopWarranty(@Nonnull UUID itemId) {
        factory.create("stopWarranty").run(() -> restClient.delete(WARRANTY_SERVICE + "/api/v1/" + itemId, Void.class).execute());
    }
}
