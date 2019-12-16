package ru.romanow.services.order.service;

import lombok.AllArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import ru.romanow.core.spring.rest.client.SpringRestClient;
import ru.romanow.services.common.config.CircuitBreakerConfiguration.Fallback;
import ru.romanow.services.order.exceptions.WarehouseProcessingException;
import ru.romanow.services.order.model.enums.SizeChart;
import ru.romanow.services.warehouse.model.ErrorResponse;
import ru.romanow.services.warehouse.model.OrderItemRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

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
    public Optional<UUID> takeItem(@Nonnull UUID orderId, @Nonnull String model, @Nonnull SizeChart size) {
        final OrderItemRequest request = new OrderItemRequest()
                .setOrderId(orderId)
                .setModel(model)
                .setSize(size.name());

        final String url = WAREHOUSE_SERVICE + "/api/v1/";
        return factory
                .create("takeItem")
                .run(() -> restClient
                             .post(url, request, UUID.class)
                             .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                             .addExceptionMapping(409, (ex) -> new WarehouseProcessingException(ex.getBody(ErrorResponse.class).getMessage()))
                             .commonErrorResponseClass(ErrorResponse.class)
                             .execute(),
                     throwable -> fallback.apply(POST, url, throwable));
    }

    @Override
    public void returnItem(@Nonnull UUID orderId, @Nonnull UUID itemId) {
        final String url = WAREHOUSE_SERVICE + "/api/v1/" + itemId;
        factory.create("returnItem")
               .run(() -> restClient.delete(url, Void.class).execute(),
                    throwable -> fallback.apply(DELETE, url, throwable));
    }

    @Nonnull
    @Override
    public Optional<OrderWarrantyResponse> useWarrantyItem(@Nonnull UUID itemId, @Nonnull OrderWarrantyRequest request) {
        final String url = WAREHOUSE_SERVICE + "/api/v1/" + itemId + "/warranty";
        return factory
                .create("useWarrantyItem")
                .run(() -> restClient
                             .post(url, request, OrderWarrantyResponse.class)
                             .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                             .addExceptionMapping(409, (ex) -> new WarehouseProcessingException(ex.getBody(ErrorResponse.class).getMessage()))
                             .commonErrorResponseClass(ErrorResponse.class)
                             .execute(),
                     throwable -> fallback.apply(POST, url, throwable, request.toString()));
    }
}
