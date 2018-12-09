package ru.romanow.services.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.romanow.services.payment.model.OrderInfoResponse;
import ru.romanow.services.store.model.PurchaseRequest;
import ru.romanow.services.store.model.WarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;

@Service
@AllArgsConstructor
public class PaymentServiceImpl
        implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private static final String PAYMENT_SERVICE = "payment-service";
    private final RestTemplate restTemplate;

    @Nonnull
    @Override
    @HystrixCommand(fallbackMethod = "getOrderInfoFallback")
    public Optional<OrderInfoResponse> getOrderInfo(@Nonnull UUID userId, @Nonnull UUID orderId) {
        return Optional.ofNullable(restTemplate.getForObject(PAYMENT_SERVICE + "/api/" + userId + "/" + orderId, OrderInfoResponse.class));
    }

    @Nonnull
    @Override
    @HystrixCommand(fallbackMethod = "getOrderInfoByUserFallback")
    public Optional<List<OrderInfoResponse>> getOrderInfoByUser(@Nonnull UUID userId) {
        final ParameterizedTypeReference<List<OrderInfoResponse>> type =
                new ParameterizedTypeReference<List<OrderInfoResponse>>() {};
        return Optional.ofNullable(restTemplate.exchange(PAYMENT_SERVICE + "/api/" + userId, HttpMethod.GET, null, type).getBody());
    }

    @Nonnull
    @Override
    @HystrixCommand
    public Optional<UUID> makePurchase(@Nonnull UUID userId, @Nonnull PurchaseRequest request) {
        return Optional.ofNullable(restTemplate.postForObject(PAYMENT_SERVICE + "/api/" + userId, request, UUID.class));
    }

    @Override
    @HystrixCommand
    public void refundPurchase(@Nonnull UUID orderId) {
        restTemplate.delete(PAYMENT_SERVICE + "/api/" + orderId);
    }

    @Nonnull
    @Override
    @HystrixCommand
    public Optional<OrderWarrantyResponse> warrantyRequest(@Nonnull UUID orderId, @Nonnull WarrantyRequest request) {
        final OrderWarrantyRequest warrantyRequest = new OrderWarrantyRequest().setReason(request.getReason());
        return Optional.ofNullable(restTemplate.postForObject(PAYMENT_SERVICE + "/api/" + orderId + "/warranty", warrantyRequest, OrderWarrantyResponse.class));
    }

    @Nonnull
    private Optional<OrderInfoResponse> getOrderInfoFallback(@Nonnull UUID userId, @Nonnull UUID orderId) {
        logger.warn("Request to GET '%s/api/%s/%s failed. Use fallback", PAYMENT_SERVICE, userId, orderId);
        return Optional.of(new OrderInfoResponse().setOrderId(orderId));
    }

    @Nonnull
    private Optional<List<OrderInfoResponse>> getOrderInfoByUserFallback(@Nonnull UUID userId) {
        logger.warn("Request to GET '%s/api/%s failed. Use fallback", PAYMENT_SERVICE, userId);
        return Optional.of(newArrayList());
    }
}
