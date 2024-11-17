/*
package tn.soa_ecommerce.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.soa_ecommerce.order.dto.OrderItemDTO;
import tn.soa_ecommerce.order.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveProducts(@RequestBody List<OrderItemDTO> products) {
        boolean isReserved = inventoryService.reserveProducts(products);
        if (isReserved) {
            return ResponseEntity.ok("Products reserved successfully");
        } else {
            return ResponseEntity.status(400).body("Failed to reserve products");
        }
    }

    @PostMapping("/release")
    public ResponseEntity<String> releaseProducts(@RequestParam String cartId) {
        inventoryService.releaseProducts(cartId);
        return ResponseEntity.ok("Products released successfully");
    }
}*/
