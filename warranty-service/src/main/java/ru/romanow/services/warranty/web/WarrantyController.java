package ru.romanow.services.warranty.web;


import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.romanow.services.warranty.modal.WarrantyInfoResponse;
import ru.romanow.services.warranty.service.WarrantyService;

import java.util.UUID;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class WarrantyController {
    private final WarrantyService warrantyService;

    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private WarrantyInfoResponse warrantyInfo(@RequestParam UUID itemId) {
        return warrantyService.getWarrantyInfo(itemId);
    }
}
