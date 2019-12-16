package ru.romanow.services.order;

import org.springframework.context.annotation.Configuration;
import ru.romanow.services.common.config.CircuitBreakerConfigurationSupport;
import ru.romanow.services.order.exceptions.WarehouseProcessingException;

import javax.persistence.EntityNotFoundException;

import static com.google.common.collect.Lists.newArrayList;

@Configuration("configurationSupport")
public class CustomCircuitBreakerConfiguration
        implements CircuitBreakerConfigurationSupport {

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Throwable>[] ignoredExceptions() {
        return newArrayList(EntityNotFoundException.class, WarehouseProcessingException.class)
                .toArray(Class[]::new);
    }
}
