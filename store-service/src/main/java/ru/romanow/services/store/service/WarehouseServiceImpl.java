package ru.romanow.services.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.romanow.services.warehouse.model.OrderInfoResponse;

import javax.annotation.Nonnull;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl
        implements WarehouseService {
    private static final String WAREHOUSE_SERVICE = "warehouse-service";
    private final RestTemplate restTemplate;

    @Override
    @HystrixCommand(fallbackMethod = "")
    public OrderInfoResponse getOrderInfo(@Nonnull UUID itemId) {
        return restTemplate.getForObject(WAREHOUSE_SERVICE + "/api/" + itemId, OrderInfoResponse.class);
    }

    private OrderInfoResponse getOrderInfoFallback(@Nonnull UUID itemId) {
        return new OrderInfoResponse();
    }
}
