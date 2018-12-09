package ru.romanow.services.payment.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.romanow.services.payment.model.enums.SizeChart;
import ru.romanow.services.warehouse.model.OrderItemRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl
        implements WarehouseService {
    private static final String WAREHOUSE_SERVICE = "warehouse-service";
    private final RestTemplate restTemplate;

    @Nonnull
    @Override
    @HystrixCommand
    public Optional<UUID> takeItem(@Nonnull UUID orderId, @Nonnull String model, @Nonnull SizeChart size) {
        final OrderItemRequest request = new OrderItemRequest()
                .setOrderId(orderId)
                .setModel(model)
                .setSize(convertToWarehouseSize(size));
        return Optional.ofNullable(restTemplate.postForObject(WAREHOUSE_SERVICE + "/api/", request, UUID.class));
    }

    @Override
    @HystrixCommand
    public void returnItem(@Nonnull UUID orderId, @Nonnull UUID itemId) {
        restTemplate.delete(WAREHOUSE_SERVICE + "/api/" + orderId + "/" + itemId);
    }

    @Override
    @HystrixCommand
    public Optional<OrderWarrantyResponse> checkWarrantyItem(@Nonnull UUID itemId, @Nonnull OrderWarrantyRequest request) {
        return Optional.ofNullable(restTemplate.postForObject(WAREHOUSE_SERVICE + "/api/" + itemId + "/warranty", request, OrderWarrantyResponse.class));
    }

    @Nonnull
    private ru.romanow.services.warehouse.model.enums.SizeChart convertToWarehouseSize(@Nonnull SizeChart size) {
        return ru.romanow.services.warehouse.model.enums.SizeChart.valueOf(size.name());
    }
}
