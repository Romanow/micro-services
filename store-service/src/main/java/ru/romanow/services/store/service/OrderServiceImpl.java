package ru.romanow.services.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.SpanName;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.romanow.services.order.model.OrderInfoResponse;
import ru.romanow.services.store.model.PurchaseRequest;
import ru.romanow.services.store.model.WarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

@Service
@AllArgsConstructor
public class OrderServiceImpl
        implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private static final String ORDER_SERVICE = "http://order-service";
    private final RestTemplate restTemplate;

    @Nonnull
    @Override
    @HystrixCommand(fallbackMethod = "getOrderInfoFallback")
    public Optional<OrderInfoResponse> getOrderInfo(@Nonnull UUID userId, @Nonnull UUID orderId) {
        return Optional.ofNullable(restTemplate.getForObject(ORDER_SERVICE + "/api/" + userId + "/" + orderId, OrderInfoResponse.class));
    }

    @Nonnull
    @Override
    @HystrixCommand(fallbackMethod = "getOrderInfoByUserFallback")
    public Optional<List<OrderInfoResponse>> getOrderInfoByUser(@Nonnull UUID userId) {
        final ParameterizedTypeReference<List<OrderInfoResponse>> type =
                new ParameterizedTypeReference<List<OrderInfoResponse>>() {};
        return Optional.ofNullable(restTemplate.exchange(ORDER_SERVICE + "/api/" + userId, HttpMethod.GET, null, type).getBody());
    }

    @Nonnull
    @Override
    @HystrixCommand
    public Optional<UUID> makePurchase(@Nonnull UUID userId, @Nonnull PurchaseRequest request) {
        return Optional.ofNullable(restTemplate.postForObject(ORDER_SERVICE + "/api/" + userId, request, UUID.class));
    }

    @Override
    @HystrixCommand
    public void refundPurchase(@Nonnull UUID orderId) {
        restTemplate.delete(ORDER_SERVICE + "/api/" + orderId);
    }

    @Nonnull
    @Override
    @HystrixCommand
    public Optional<OrderWarrantyResponse> warrantyRequest(@Nonnull UUID orderId, @Nonnull WarrantyRequest request) {
        final OrderWarrantyRequest warrantyRequest = new OrderWarrantyRequest().setReason(request.getReason());
        return Optional.ofNullable(restTemplate.postForObject(ORDER_SERVICE + "/api/" + orderId + "/warranty", warrantyRequest, OrderWarrantyResponse.class));
    }

    @Nonnull
    private Optional<OrderInfoResponse> getOrderInfoFallback(@Nonnull UUID userId, @Nonnull UUID orderId, Throwable throwable) {
        logger.warn("Request to GET '{}/api/%s/{} failed with exception: {}. Use fallback", ORDER_SERVICE, userId, orderId, throwable.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("", throwable);
        }
        return Optional.empty();
    }

    @Nonnull
    private Optional<List<OrderInfoResponse>> getOrderInfoByUserFallback(@Nonnull UUID userId, Throwable throwable) {
        logger.warn("Request to GET '{}/api/{} failed with exception: {}. Use fallback", ORDER_SERVICE, userId, throwable.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("", throwable);
        }
        return Optional.of(newArrayList());
    }
}
