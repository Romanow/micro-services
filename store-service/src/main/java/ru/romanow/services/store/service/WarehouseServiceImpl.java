package ru.romanow.services.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.romanow.services.warehouse.model.OrderInfoResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl
        implements WarehouseService {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);

    private static final String WAREHOUSE_SERVICE = "warehouse-service";
    private final RestTemplate restTemplate;

    @Nullable
    @Override
    @HystrixCommand(fallbackMethod = "getOrderInfoFallback")
    public OrderInfoResponse getOrderInfo(@Nonnull UUID itemId) {
        return restTemplate.getForObject(WAREHOUSE_SERVICE + "/api/" + itemId, OrderInfoResponse.class);
    }

    private OrderInfoResponse getOrderInfoFallback(@Nonnull UUID itemId) {
        logger.warn("Request to '%s/api/%s/%s failed. Use fallback", WAREHOUSE_SERVICE, itemId);
        return new OrderInfoResponse().setItemId(itemId);
    }
}
