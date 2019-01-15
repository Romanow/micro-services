package ru.romanow.services.store.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final SpringRestClient restClient;

    @Nonnull
    @Override
    @HystrixCommand(fallbackMethod = "getOrderInfoFallback")
    public Optional<OrderInfoResponse> getOrderInfo(@Nonnull UUID userId, @Nonnull UUID orderId) {
        return restClient
                .get(ORDER_SERVICE + "/api/" + userId + "/" + orderId, OrderInfoResponse.class)
                .execute();
    }

    @Nonnull
    @Override
    @HystrixCommand(fallbackMethod = "getOrderInfoByUserFallback")
    public Optional<OrdersInfoResponse> getOrderInfoByUser(@Nonnull UUID userId) {
        return restClient
                .get(ORDER_SERVICE + "/api/" + userId, OrdersInfoResponse.class)
                .execute();
    }

    @Nonnull
    @Override
    @HystrixCommand(ignoreExceptions = { EntityNotFoundException.class, OrderProcessException.class })
    public Optional<UUID> makePurchase(@Nonnull UUID userId, @Nonnull PurchaseRequest request) {
        return restClient
                .post(ORDER_SERVICE + "/api/" + userId, request, UUID.class)
                .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                .addExceptionMapping(409, (ex) -> new OrderProcessException(ex.getBody(ErrorResponse.class).getMessage()))
                .commonErrorResponseClass(ErrorResponse.class)
                .execute();
    }

    @Override
    @HystrixCommand(ignoreExceptions = EntityNotFoundException.class)
    public void refundPurchase(@Nonnull UUID orderId) {
        restClient
                .delete(ORDER_SERVICE + "/api/" + orderId, Void.class)
                .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                .commonErrorResponseClass(ErrorResponse.class)
                .execute();
    }

    @Nonnull
    @Override
    @HystrixCommand(ignoreExceptions = { EntityNotFoundException.class, OrderProcessException.class })
    public Optional<OrderWarrantyResponse> warrantyRequest(@Nonnull UUID orderId, @Nonnull WarrantyRequest request) {
        final OrderWarrantyRequest warrantyRequest = new OrderWarrantyRequest().setReason(request.getReason());
        return restClient
                .post(ORDER_SERVICE + "/api/" + orderId + "/warranty", warrantyRequest, OrderWarrantyResponse.class)
                .addExceptionMapping(404, (ex) -> new EntityNotFoundException(ex.getBody(ErrorResponse.class).getMessage()))
                .addExceptionMapping(409, (ex) -> new OrderProcessException(ex.getBody(ErrorResponse.class).getMessage()))
                .commonErrorResponseClass(ErrorResponse.class)
                .execute();
    }

    @Nonnull
    private Optional<OrderInfoResponse> getOrderInfoFallback(@Nonnull UUID userId, @Nonnull UUID orderId, Throwable throwable) {
        logger.warn("Request to GET '{}/api/{}/{}' failed with exception: {}. Use fallback", ORDER_SERVICE, userId, orderId, throwable.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("", throwable);
        }
        return Optional.empty();
    }

    @Nonnull
    private Optional<OrdersInfoResponse> getOrderInfoByUserFallback(@Nonnull UUID userId, Throwable throwable) {
        logger.warn("Request to GET '{}/api/{}' failed with exception: {}. Use fallback", ORDER_SERVICE, userId, throwable.getMessage());
        if (logger.isDebugEnabled()) {
            logger.debug("", throwable);
        }
        return Optional.of(new OrdersInfoResponse());
    }
}
