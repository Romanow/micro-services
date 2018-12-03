package ru.romanow.services.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.romanow.services.payment.model.PaymentInfoResponse;

import javax.annotation.Nonnull;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl
        implements PaymentService {
    private final RestTemplate restTemplate;

    @Override
    @HystrixCommand(fallbackMethod = "")
    public PaymentInfoResponse getPaymentInfoByOrder(@Nonnull UUID userId, @Nonnull UUID orderId) {
        return null;
    }
}
