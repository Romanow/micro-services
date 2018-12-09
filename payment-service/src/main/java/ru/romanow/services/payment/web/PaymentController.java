package ru.romanow.services.payment.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.romanow.services.payment.model.CreateOrderRequest;
import ru.romanow.services.payment.model.OrderInfoResponse;
import ru.romanow.services.payment.service.OrderService;
import ru.romanow.services.payment.service.PaymentService;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    @GetMapping(value = "/{userId}/{orderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private OrderInfoResponse userOrder(@RequestParam UUID userId, @RequestParam UUID orderId) {
        return paymentService.getUserOrder(userId, orderId);
    }

    @GetMapping(value = "/{userId}/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private List<OrderInfoResponse> userOrders(@RequestParam UUID userId) {
        return paymentService.getUserOrders(userId);
    }

    @PostMapping(value = "/{userId}/",
                 consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                 produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private UUID makeOrder(@RequestParam UUID userId,
                           @RequestBody @Valid CreateOrderRequest request) {
        return orderService.makeOrder(userId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private void refundOrder(@RequestParam UUID orderId) {
        orderService.refundOrder(orderId);
    }

    @PostMapping(value = "/{orderId}/warranty",
                 consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                 produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private OrderWarrantyResponse warranty(@RequestParam UUID orderId,
                                           @RequestBody @Valid OrderWarrantyRequest request) {
        return orderService.checkWarranty(orderId, request);
    }
}
