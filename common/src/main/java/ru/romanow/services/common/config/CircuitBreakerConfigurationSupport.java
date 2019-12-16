package ru.romanow.services.common.config;

public interface CircuitBreakerConfigurationSupport {

    @SuppressWarnings("unchecked")
    default Class<? extends Throwable>[] ignoredExceptions() {
        return new Class[0];
    }
}
