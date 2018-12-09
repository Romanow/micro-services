package ru.romanow.services.order.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.romanow.services.order.service.OrderManagementService;
import ru.romanow.services.order.model.CreateOrderRequest;
import ru.romanow.services.order.model.OrderInfoResponse;
import ru.romanow.services.order.service.OrderService;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderManagementService orderManagementService;

    @GetMapping(value = "/{userId}/{orderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private OrderInfoResponse userOrder(@RequestParam UUID userId, @RequestParam UUID orderId) {
        return orderService.getUserOrder(userId, orderId);
    }

    @GetMapping(value = "/{userId}/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private List<OrderInfoResponse> userOrders(@RequestParam UUID userId) {
        return orderService.getUserOrders(userId);
    }

    @PostMapping(value = "/{userId}/",
                 consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                 produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private UUID makeOrder(@RequestParam UUID userId,
                           @RequestBody @Valid CreateOrderRequest request) {
        return orderManagementService.makeOrder(userId, request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{orderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private void refundOrder(@RequestParam UUID orderId) {
        orderManagementService.refundOrder(orderId);
    }

    @PostMapping(value = "/{orderId}/warranty",
                 consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                 produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private OrderWarrantyResponse warranty(@RequestParam UUID orderId,
                                           @RequestBody @Valid OrderWarrantyRequest request) {
        return orderManagementService.checkWarranty(orderId, request);
    }
}
