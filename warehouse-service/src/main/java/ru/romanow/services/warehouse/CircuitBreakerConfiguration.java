package ru.romanow.services.warehouse;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityNotFoundException;
import java.time.Duration;

@Configuration
public class CircuitBreakerConfiguration {
    private static final int REQUEST_TIMEOUT = 100;

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        final TimeLimiterConfig timeLimiterConfig =
                TimeLimiterConfig
                        .custom()
                        .timeoutDuration(Duration.ofSeconds(REQUEST_TIMEOUT))
                        .build();
        final CircuitBreakerConfig circuitBreakerConfig =
                CircuitBreakerConfig
                        .custom()
                        .ignoreExceptions(EntityNotFoundException.class)
                        .build();
        return factory ->
                factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                        .timeLimiterConfig(timeLimiterConfig)
                        .circuitBreakerConfig(circuitBreakerConfig)
                        .build());
    }
}
