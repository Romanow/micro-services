package ru.romanow.services.common.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Configuration
public class CircuitBreakerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerConfiguration.class);

    private static final int REQUEST_TIMEOUT = 100;

    @Bean
    @ConditionalOnMissingBean(CircuitBreakerConfigurationSupport.class)
    public CircuitBreakerConfigurationSupport configurationSupport() {
        return new CircuitBreakerConfigurationSupport() {};
    }

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
                        .ignoreExceptions(configurationSupport().ignoredExceptions())
                        .build();
        return factory ->
                factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                        .timeLimiterConfig(timeLimiterConfig)
                        .circuitBreakerConfig(circuitBreakerConfig)
                        .build());
    }

    @FunctionalInterface
    public interface Fallback {
        <T> Optional<T> apply(@Nonnull HttpMethod method, @Nonnull String url, Throwable throwable, Object ... params);
    }

    @Bean
    public Fallback fallback() {
        return new Fallback() {
            @Override
            public <T> Optional<T> apply(@Nonnull HttpMethod method, @Nonnull String url, Throwable throwable, Object ... params) {
                logger.warn("Request to {} '{}' failed with exception: {}. (params '{}')", method.name(), url, throwable.getMessage(), params);
                if (List.of(configurationSupport().ignoredExceptions()).contains(throwable.getClass())) {
                    throw (RuntimeException)throwable;
                }

                logger.debug("", throwable);
                return Optional.empty();
            }
        };
    }
}
