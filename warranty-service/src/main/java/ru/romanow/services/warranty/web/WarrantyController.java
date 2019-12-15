package ru.romanow.services.warranty.web;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.romanow.services.warranty.modal.ItemWarrantyRequest;
import ru.romanow.services.warranty.modal.OrderWarrantyResponse;
import ru.romanow.services.warranty.modal.WarrantyInfoResponse;
import ru.romanow.services.warranty.service.WarrantyService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class WarrantyController {
    private final WarrantyService warrantyService;

    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    private WarrantyInfoResponse warrantyInfo(@PathVariable UUID itemId) {
        return warrantyService.getWarrantyInfo(itemId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{itemId}")
    private void startWarranty(@PathVariable UUID itemId) {
        warrantyService.startWarranty(itemId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{itemId}")
    public void stopWarranty(@PathVariable UUID itemId) {
        warrantyService.stopWarranty(itemId);
    }

    @PostMapping(value = "/{itemId}/warranty",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderWarrantyResponse warrantyRequest(@PathVariable UUID itemId, @RequestBody @Valid ItemWarrantyRequest request) {
        return warrantyService.warrantyRequest(itemId, request);
    }
}
