package ru.romanow.services.store.web;

import com.google.common.net.HttpHeaders;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.romanow.services.store.model.*;
import ru.romanow.services.store.service.OrderService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

import static java.lang.String.format;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class StoreController {
    private OrderService orderService;

    @GetMapping(value = "/{userId}/orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private UserOrdersResponse orders(@RequestParam UUID userId) {
        return orderService.findUserOrders(userId);
    }

    @GetMapping("/{userId}/{orderId}")
    private UserOrderResponse orders(@RequestParam UUID userId, @RequestParam UUID orderId) {
        return orderService.findUserOrder(userId, orderId);
    }

    @PostMapping(value = "/{userId}/purchase",
                 consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                 produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private void purchase(@RequestParam UUID userId,
                                      @RequestBody @Valid PurchaseRequest request,
                                      HttpServletResponse servletResponse) {
        final UUID orderId = orderService.makePurchase(userId, request);
        servletResponse.setHeader(HttpHeaders.LOCATION, format("/%s/%s/", userId, orderId));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{userId}/{orderId}/refund",
                   consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                   produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private void purchase(@RequestParam UUID userId,
                          @RequestParam UUID orderId) {
        orderService.refundPurchase(userId, orderId);
    }

    @PostMapping(value = "/{userId}/{orderId}/warranty",
                 consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                 produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private WarrantyResponse purchase(@RequestParam UUID userId,
                                      @RequestParam UUID orderId,
                                      @RequestBody @Valid WarrantyRequest request) {
        return orderService.warrantyRequest(userId, orderId, request);
    }
}
