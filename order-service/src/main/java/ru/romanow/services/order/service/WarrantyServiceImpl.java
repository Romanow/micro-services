package ru.romanow.services.order.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarrantyServiceImpl
        implements WarrantyService {
    private static final String WARRANTY_SERVICE = "warranty-service";
    private final RestTemplate restTemplate;

    @Override
    @HystrixCommand
    public void startWarranty(@Nonnull UUID itemId) {
        restTemplate.postForObject(WARRANTY_SERVICE + "/api/" + itemId, null, Void.class);
    }

    @Override
    @HystrixCommand
    public void stopWarranty(@Nonnull UUID itemId) {
        restTemplate.delete(WARRANTY_SERVICE + "/api/" + itemId);
    }
}
