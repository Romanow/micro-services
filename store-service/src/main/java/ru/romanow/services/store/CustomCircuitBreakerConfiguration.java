package ru.romanow.services.store;

import org.springframework.context.annotation.Configuration;
import ru.romanow.services.common.config.CircuitBreakerConfigurationSupport;
import ru.romanow.services.store.exceptions.OrderProcessException;

import javax.persistence.EntityNotFoundException;

import static com.google.common.collect.Lists.newArrayList;

@Configuration("configurationSupport")
public class CustomCircuitBreakerConfiguration
        implements CircuitBreakerConfigurationSupport {

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends Throwable>[] ignoredExceptions() {
        return newArrayList(EntityNotFoundException.class, OrderProcessException.class)
                .toArray(Class[]::new);
    }
}
