package ru.romanow.services.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.romanow.services.payment.model.PaymentInfoResponse;

import javax.annotation.Nonnull;
import java.util.ArrayList;
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
    @HystrixCommand(fallbackMethod = "getPaymentInfoByOrderFallback")
    public Optional<PaymentInfoResponse> getPaymentInfoByOrder(@Nonnull UUID userId, @Nonnull UUID orderId) {
        return Optional.ofNullable(restTemplate.getForObject(PAYMENT_SERVICE + "/api/" + userId + "/" + orderId, PaymentInfoResponse.class));
    }

    @Nonnull
    @Override
    @HystrixCommand(fallbackMethod = "getPaymentInfoByUserFallback")
    public Optional<List<PaymentInfoResponse>> getPaymentInfoByUser(@Nonnull UUID userId) {
        final ParameterizedTypeReference<List<PaymentInfoResponse>> type =
                new ParameterizedTypeReference<List<PaymentInfoResponse>>() {};
        return Optional.ofNullable(restTemplate.exchange(PAYMENT_SERVICE + "/api/" + userId, HttpMethod.GET, null, type).getBody());
    }

    @Nonnull
    private Optional<PaymentInfoResponse> getPaymentInfoByOrderFallback(@Nonnull UUID userId, @Nonnull UUID orderId) {
        logger.warn("Request to '%s/api/%s/%s failed. Use fallback", PAYMENT_SERVICE, userId, orderId);
        return Optional.of(new PaymentInfoResponse().setOrderId(orderId));
    }

    @Nonnull
    public Optional<List<PaymentInfoResponse>> getPaymentInfoByUserFallback(@Nonnull UUID userId) {
        logger.warn("Request to '%s/api/%s failed. Use fallback", PAYMENT_SERVICE, userId);
        return Optional.of(newArrayList());
    }
}
