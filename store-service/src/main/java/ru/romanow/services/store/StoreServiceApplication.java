package ru.romanow.services.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;
import ru.romanow.services.common.config.CircuitBreakerConfiguration;
import ru.romanow.services.common.config.SecurityConfiguration;
import ru.romanow.services.common.config.SwaggerConfiguration;
import ru.romanow.services.common.config.WebConfiguration;

@Import({
        CircuitBreakerConfiguration.class,
        SecurityConfiguration.class,
        SwaggerConfiguration.class,
        WebConfiguration.class
})
@EnableEurekaClient
@SpringBootApplication
public class StoreServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StoreServiceApplication.class, args);
    }
}

