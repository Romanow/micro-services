package ru.romanow.services.order.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanow.core.spring.rest.client.SpringRestClient;
import ru.romanow.services.order.exceptions.EntityProcessException;
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
    private final SpringRestClient restClient;

    @Nonnull
    @Override
    @HystrixCommand(ignoreExceptions = { EntityNotFoundException.class, EntityProcessException.class })
    public Optional<UUID> takeItem(@Nonnull UUID orderId, @Nonnull String model, @Nonnull SizeChart size) {
        final OrderItemRequest request = new OrderItemRequest()
                .setOrderId(orderId)
                .setModel(model)
                .setSize(convertToWarehouseSize(size));
        return restClient
                .post(WAREHOUSE_SERVICE + "/api/", request, UUID.class)
                .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                .addExceptionMapping(409, (ex) -> new EntityProcessException(ex.getBody(ErrorResponse.class).getMessage()))
                .commonErrorResponseClass(ErrorResponse.class)
                .execute();
    }

    @Override
    @HystrixCommand
    public void returnItem(@Nonnull UUID orderId, @Nonnull UUID itemId) {
        restClient.delete(WAREHOUSE_SERVICE + "/api/" + itemId, Void.class).execute();
    }

    @Nonnull
    @Override
    @HystrixCommand(ignoreExceptions = { EntityNotFoundException.class, EntityProcessException.class })
    public Optional<OrderWarrantyResponse> checkWarrantyItem(@Nonnull UUID itemId, @Nonnull OrderWarrantyRequest request) {
        return restClient
                .post(WAREHOUSE_SERVICE + "/api/" + itemId + "/warranty", request, OrderWarrantyResponse.class)
                .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                .addExceptionMapping(409, (ex) -> new EntityProcessException(ex.getBody(ErrorResponse.class).getMessage()))
                .commonErrorResponseClass(ErrorResponse.class)
                .execute();
    }

    @Nonnull
    private ru.romanow.services.warehouse.model.enums.SizeChart convertToWarehouseSize(@Nonnull SizeChart size) {
        return ru.romanow.services.warehouse.model.enums.SizeChart.valueOf(size.name());
    }
}
