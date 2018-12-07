package ru.romanow.services.warehouse.web;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.romanow.services.warehouse.model.ItemInfoResponse;
import ru.romanow.services.warehouse.service.WarehouseService;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private ItemInfoResponse itemInfo(@RequestParam UUID itemId) {
        return warehouseService.getItemInfo(itemId);
    }
}
