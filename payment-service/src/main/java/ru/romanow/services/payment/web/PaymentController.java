package ru.romanow.services.payment.web;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.romanow.services.payment.model.PaymentInfoResponse;
import ru.romanow.services.payment.service.PaymentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping(value = "/{userId}/{orderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private PaymentInfoResponse userOrder(@RequestParam UUID userId, @RequestParam UUID orderId) {
        return paymentService.getUserOrder(userId, orderId);
    }

    @GetMapping(value = "/{userId}/}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private List<PaymentInfoResponse> userOrders(@RequestParam UUID userId) {
        return paymentService.getUserOrders(userId);
    }
}
