package ru.romanow.services.order.service;

import lombok.AllArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import ru.romanow.core.spring.rest.client.SpringRestClient;
import ru.romanow.services.common.config.CircuitBreakerConfiguration.Fallback;

import javax.annotation.Nonnull;
import java.util.UUID;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

@Service
@AllArgsConstructor
public class WarrantyServiceImpl
        implements WarrantyService {
    private static final String WARRANTY_SERVICE = "http://warranty-service";

    private final CircuitBreakerFactory factory;
    private final SpringRestClient restClient;
    private final Fallback fallback;

    @Override
    public void startWarranty(@Nonnull UUID itemId) {
        final String url = WARRANTY_SERVICE + "/api/v1/" + itemId;
        factory.create("startWarranty")
               .run(() -> restClient.post(url, null, Void.class).execute(),
                    throwable -> fallback.apply(POST, url, throwable));
    }

    @Override
    public void stopWarranty(@Nonnull UUID itemId) {
        final String url = WARRANTY_SERVICE + "/api/v1/" + itemId;
        factory.create("stopWarranty")
               .run(() -> restClient.delete(url, Void.class).execute(),
                    throwable -> fallback.apply(DELETE, url, throwable));
    }
}
