package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import ru.romanow.core.spring.rest.client.SpringRestClient;
import ru.romanow.services.order.model.ErrorResponse;
import ru.romanow.services.order.model.OrderInfoResponse;
import ru.romanow.services.order.model.OrdersInfoResponse;
import ru.romanow.services.store.exceptions.OrderProcessException;
import ru.romanow.services.store.model.PurchaseRequest;
import ru.romanow.services.store.model.WarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderServiceImpl
        implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private static final String ORDER_SERVICE = "http://order-service";

    private final CircuitBreakerFactory factory;
    private final SpringRestClient restClient;

    @Nonnull
    @Override
    public Optional<OrderInfoResponse> getOrderInfo(@Nonnull UUID userId, @Nonnull UUID orderId) {
        return factory
                .create("getOrderInfo")
                .run(() -> restClient
                             .get(ORDER_SERVICE + "/api/v1/" + userId + "/" + orderId, OrderInfoResponse.class)
                             .execute(),
                     throwable -> getOrderInfoFallback(userId, orderId, throwable));
    }

    @Nonnull
    @Override
    public Optional<OrdersInfoResponse> getOrderInfoByUser(@Nonnull UUID userId) {
        return factory
                .create("getOrderInfoByUser")
                .run(() -> restClient
                             .get(ORDER_SERVICE + "/api/v1/" + userId, OrdersInfoResponse.class)
                             .execute(),
                     throwable -> getOrderInfoByUserFallback(userId, throwable));
    }

    @Nonnull
    @Override
    public Optional<UUID> makePurchase(@Nonnull UUID userId, @Nonnull PurchaseRequest request) {
        return factory
                .create("makePurchase")
                .run(() -> restClient
                             .post(ORDER_SERVICE + "/api/v1/" + userId, request, UUID.class)
                             .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                             .addExceptionMapping(409, (ex) -> new OrderProcessException(ex.getBody(ErrorResponse.class).getMessage()))
                             .commonErrorResponseClass(ErrorResponse.class)
                             .execute(),
                     throwable -> defaultFallback(throwable));
    }

    private Optional<UUID> defaultFallback(Throwable throwable) {
        if (throwable instanceof EntityNotFoundException || throwable instanceof OrderProcessException)
            throw (RuntimeException)throwable;
        return Optional.empty();
    }

    @Override
    public void refundPurchase(@Nonnull UUID orderId) {
        factory.create("refundPurchase")
               .run(() -> restClient
                       .delete(ORDER_SERVICE + "/api/v1/" + orderId, Void.class)
                       .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                       .commonErrorResponseClass(ErrorResponse.class)
                       .execute());
    }

    @Nonnull
    @Override
    public Optional<OrderWarrantyResponse> warrantyRequest(@Nonnull UUID orderId, @Nonnull WarrantyRequest request) {
        final OrderWarrantyRequest warrantyRequest = new OrderWarrantyRequest().setReason(request.getReason());
        return factory
                .create("warrantyRequest")
                .run(() -> restClient
                        .post(ORDER_SERVICE + "/api/v1/" + orderId + "/warranty", warrantyRequest, OrderWarrantyResponse.class)
                        .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                        .addExceptionMapping(409, (ex) -> new OrderProcessException(ex.getBody(ErrorResponse.class).getMessage()))
                        .commonErrorResponseClass(ErrorResponse.class)
                        .execute());
    }

    @Nonnull
    private Optional<OrderInfoResponse> getOrderInfoFallback(@Nonnull UUID userId, @Nonnull UUID orderId, Throwable throwable) {
        logger.warn("Request to GET '{}/api/v1/{}/{}' failed with exception: {}. Use fallback", ORDER_SERVICE, userId, orderId, throwable.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("", throwable);
        }
        return Optional.empty();
    }

    @Nonnull
    private Optional<OrdersInfoResponse> getOrderInfoByUserFallback(@Nonnull UUID userId, Throwable throwable) {
        logger.warn("Request to GET '{}/api/v1/{}' failed with exception: {}. Use fallback", ORDER_SERVICE, userId, throwable.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("", throwable);
        }
        return Optional.of(new OrdersInfoResponse());
    }
}
