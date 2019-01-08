package ru.romanow.services.warehouse.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.romanow.services.warehouse.model.OrderItemInfoResponse;
import ru.romanow.services.warehouse.model.OrderItemRequest;
import ru.romanow.services.warehouse.service.WarehouseService;
import ru.romanow.services.warehouse.service.WarrantyService;
import ru.romanow.services.warranty.modal.OrderWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final WarrantyService warrantyService;

    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private OrderItemInfoResponse item(@PathVariable UUID itemId) {
        return warehouseService.getItemInfo(itemId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UUID takeItem(@Valid @RequestBody OrderItemRequest request) {
        return warehouseService.takeItem(request);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{itemId}")
    public void returnItem(@PathVariable UUID itemId) {
        warehouseService.returnItem(itemId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{itemId}/warranty")
    public OrderWarrantyResponse warranty(@PathVariable UUID itemId, @RequestBody @Valid OrderWarrantyRequest request) {
        return warrantyService.warrantyRequest(itemId, request);
    }
}
