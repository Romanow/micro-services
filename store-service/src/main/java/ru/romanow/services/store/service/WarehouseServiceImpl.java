package ru.romanow.services.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
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
    private final RestTemplate restTemplate;

    @Nonnull
    @Override
    @HystrixCommand(fallbackMethod = "getOrderInfoFallback")
    public Optional<OrderItemInfoResponse> getItemInfo(@Nonnull UUID itemId) {
        return Optional.ofNullable(restTemplate.getForObject(WAREHOUSE_SERVICE + "/api/" + itemId, OrderItemInfoResponse.class));
    }

    private Optional<OrderItemInfoResponse> getOrderInfoFallback(@Nonnull UUID itemId, Throwable throwable) {
        logger.warn("Request to '{}/api/{}/{} failed with exception: {}. Use fallback", WAREHOUSE_SERVICE, itemId, throwable.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("", throwable);
        }
        return Optional.of(new OrderItemInfoResponse().setItemId(itemId));
    }
}
