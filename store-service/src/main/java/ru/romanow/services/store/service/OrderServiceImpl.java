package ru.romanow.services.store.service;

import lombok.AllArgsConstructor;
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

import static org.springframework.http.HttpMethod.*;
import static ru.romanow.services.common.config.CircuitBreakerConfiguration.Fallback;

@Service
@AllArgsConstructor
public class OrderServiceImpl
        implements OrderService {
    private static final String ORDER_SERVICE = "http://order-service";

    private final CircuitBreakerFactory factory;
    private final SpringRestClient restClient;
    private final Fallback fallback;

    @Nonnull
    @Override
    public Optional<OrderInfoResponse> getOrderInfo(@Nonnull UUID userId, @Nonnull UUID orderId) {
        final String url = ORDER_SERVICE + "/api/v1/" + userId + "/" + orderId;
        return factory
                .create("getOrderInfo")
                .run(() -> restClient.get(url, OrderInfoResponse.class).execute(),
                     throwable -> fallback.apply(GET, url, throwable));
    }

    @Nonnull
    @Override
    public Optional<OrdersInfoResponse> getOrderInfoByUser(@Nonnull UUID userId) {
        final String url = ORDER_SERVICE + "/api/v1/" + userId;
        return factory
                .create("getOrderInfoByUser")
                .run(() -> restClient.get(url, OrdersInfoResponse.class).execute(),
                     throwable -> fallback.apply(GET, url, throwable));
    }

    @Nonnull
    @Override
    public Optional<UUID> makePurchase(@Nonnull UUID userId, @Nonnull PurchaseRequest request) {
        final String url = ORDER_SERVICE + "/api/v1/" + userId;
        return factory
                .create("makePurchase")
                .run(() -> restClient
                             .post(url, request, UUID.class)
                             .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                             .addExceptionMapping(409, (ex) -> new OrderProcessException(ex.getBody(ErrorResponse.class).getMessage()))
                             .commonErrorResponseClass(ErrorResponse.class)
                             .execute(),
                     throwable -> fallback.apply(POST, url, throwable, request.toString()));
    }

    @Override
    public void refundPurchase(@Nonnull UUID orderId) {
        final String url = ORDER_SERVICE + "/api/v1/" + orderId;
        factory.create("refundPurchase")
               .run(() -> restClient
                            .delete(url, Void.class)
                            .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                            .commonErrorResponseClass(ErrorResponse.class)
                            .execute(),
                    throwable -> fallback.apply(DELETE, url, throwable));
    }

    @Nonnull
    @Override
    public Optional<OrderWarrantyResponse> warrantyRequest(@Nonnull UUID orderId, @Nonnull WarrantyRequest request) {
        final OrderWarrantyRequest warrantyRequest = new OrderWarrantyRequest().setReason(request.getReason());
        final String url = ORDER_SERVICE + "/api/v1/" + orderId + "/warranty";
        return factory
                .create("warrantyRequest")
                .run(() -> restClient
                        .post(url, warrantyRequest, OrderWarrantyResponse.class)
                        .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                        .addExceptionMapping(409, (ex) -> new OrderProcessException(ex.getBody(ErrorResponse.class).getMessage()))
                        .commonErrorResponseClass(ErrorResponse.class)
                        .execute(),
                     throwable -> fallback.apply(POST, url, throwable, warrantyRequest.toString()));
    }
}
