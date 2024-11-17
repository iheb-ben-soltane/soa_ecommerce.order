/*
package tn.soa_ecommerce.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.soa_ecommerce.order.service.ShippingService;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    private final ShippingService shippingService;

    public ShippingController(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<String> scheduleShipping(@RequestParam String cartId, @RequestParam String userId, @RequestParam String address) {
        String trackingId = shippingService.scheduleShipping(cartId, userId, address);
        return ResponseEntity.ok("Shipping scheduled successfully, Tracking ID: " + trackingId);
    }
}*/
