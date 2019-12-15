package ru.romanow.services.store;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey.Factory;
import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.netflix.hystrix.HystrixCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfiguration {
    private static final int EXECUTION_TIMEOUT = 10_000;

    @Bean
    public Customizer<HystrixCircuitBreakerFactory> defaultConfig() {
        return factory -> factory
                .configureDefault(id -> HystrixCommand.Setter
                        .withGroupKey(Factory.asKey(id))
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties
                                        .Setter()
                                        .withExecutionTimeoutInMilliseconds(EXECUTION_TIMEOUT)));
    }
}
