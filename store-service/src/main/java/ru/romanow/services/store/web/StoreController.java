package ru.romanow.services.store.web;

import com.google.common.net.HttpHeaders;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.romanow.services.store.model.*;
import ru.romanow.services.store.service.StoreService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

import static java.lang.String.format;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class StoreController {
    private StoreService storeService;

    @GetMapping(value = "/{userId}/orders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private UserOrdersResponse orders(@PathVariable UUID userId) {
        return storeService.findUserOrders(userId);
    }

    @GetMapping("/{userId}/{orderId}")
    private UserOrderResponse orders(@PathVariable UUID userId, @PathVariable UUID orderId) {
        return storeService.findUserOrder(userId, orderId);
    }

    @PostMapping(value = "/{userId}/purchase",
                 consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                 produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private void purchase(@PathVariable UUID userId,
                          @RequestBody @Valid PurchaseRequest request,
                          HttpServletResponse servletResponse) {
        final UUID orderId = storeService.makePurchase(userId, request);
        servletResponse.setHeader(HttpHeaders.LOCATION, format("/%s/%s/", userId, orderId));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{userId}/{orderId}/refund",
                   consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                   produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private void purchase(@PathVariable UUID userId,
                          @PathVariable UUID orderId) {
        storeService.refundPurchase(userId, orderId);
    }

    @PostMapping(value = "/{userId}/{orderId}/warranty",
                 consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
                 produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private WarrantyResponse warranty(@PathVariable UUID userId,
                                      @PathVariable UUID orderId,
                                      @RequestBody @Valid WarrantyRequest request) {
        return storeService.warrantyRequest(userId, orderId, request);
    }
}
