package ru.romanow.services.order.service;

import lombok.AllArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import ru.romanow.core.spring.rest.client.SpringRestClient;
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

@Service
@AllArgsConstructor
public class WarehouseServiceImpl
        implements WarehouseService {
    private static final String WAREHOUSE_SERVICE = "http://warehouse-service";

    private final CircuitBreakerFactory factory;
    private final SpringRestClient restClient;

    @Nonnull
    @Override
    public Optional<UUID> takeItem(@Nonnull UUID orderId, @Nonnull String model, @Nonnull SizeChart size) {
        final OrderItemRequest request = new OrderItemRequest()
                .setOrderId(orderId)
                .setModel(model)
                .setSize(size.name());

        return factory
                .create("takeItem")
                .run(() -> restClient
                        .post(WAREHOUSE_SERVICE + "/api/v1/", request, UUID.class)
                        .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                        .addExceptionMapping(409, (ex) -> new WarehouseProcessingException(ex.getBody(ErrorResponse.class).getMessage()))
                        .commonErrorResponseClass(ErrorResponse.class)
                        .execute(),
                     throwable -> fallback(throwable));
    }

    private Optional<UUID> fallback(Throwable throwable) {
        if (throwable instanceof EntityNotFoundException || throwable instanceof WarehouseProcessingException) {
            throw (RuntimeException)throwable;
        }
        return Optional.empty();
    }

    @Override
    public void returnItem(@Nonnull UUID orderId, @Nonnull UUID itemId) {
        factory.create("returnItem").run(() -> restClient.delete(WAREHOUSE_SERVICE + "/api/v1/" + itemId, Void.class).execute());
    }

    @Nonnull
    @Override
    public Optional<OrderWarrantyResponse> useWarrantyItem(@Nonnull UUID itemId, @Nonnull OrderWarrantyRequest request) {
        return factory
                .create("useWarrantyItem")
                .run(() -> restClient
                        .post(WAREHOUSE_SERVICE + "/api/v1/" + itemId + "/warranty", request, OrderWarrantyResponse.class)
                        .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                        .addExceptionMapping(409, (ex) -> new WarehouseProcessingException(ex.getBody(ErrorResponse.class).getMessage()))
                        .commonErrorResponseClass(ErrorResponse.class)
                        .execute());
    }
}
