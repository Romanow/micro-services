package ru.romanow.services.order.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.romanow.core.spring.rest.client.SpringRestClient;

import javax.annotation.Nonnull;
import java.util.UUID;

@Service
@AllArgsConstructor
public class WarrantyServiceImpl
        implements WarrantyService {
    private static final String WARRANTY_SERVICE = "http://warranty-service";
    private final SpringRestClient restClient;

    @Override
    @HystrixCommand
    public void startWarranty(@Nonnull UUID itemId) {
        restClient.post(WARRANTY_SERVICE + "/api/" + itemId, null, Void.class).execute();
    }

    @Override
    @HystrixCommand
    public void stopWarranty(@Nonnull UUID itemId) {
        restClient.delete(WARRANTY_SERVICE + "/api/" + itemId, Void.class).execute();
    }
}
