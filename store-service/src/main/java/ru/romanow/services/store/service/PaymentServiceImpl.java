package ru.romanow.services.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.romanow.services.payment.model.PaymentInfoResponse;
import ru.romanow.services.warehouse.model.OrderInfoResponse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl
        implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private static final String PAYMENT_SERVICE = "payment-service";
    private final RestTemplate restTemplate;

    @Nullable
    @Override
    @HystrixCommand(fallbackMethod = "getPaymentInfoByOrderFallback")
    public PaymentInfoResponse getPaymentInfoByOrder(@Nonnull UUID userId, @Nonnull UUID orderId) {
        return restTemplate.getForObject(PAYMENT_SERVICE + "/api/" + userId + "/" + orderId, PaymentInfoResponse.class);
    }

    private PaymentInfoResponse getPaymentInfoByOrderFallback(@Nonnull UUID userId, @Nonnull UUID orderId) {
        logger.warn("Request to '%s/api/%s/%s failed. Use fallback", PAYMENT_SERVICE, userId, orderId);
        return new PaymentInfoResponse().setOrderId(orderId);
    }
}
