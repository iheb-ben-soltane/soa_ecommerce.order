/*
package tn.soa_ecommerce.order.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.soa_ecommerce.order.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestParam String cartId, @RequestParam String userId, @RequestParam double amount) {
        String transactionId = paymentService.processPayment(cartId, userId, amount);
        return ResponseEntity.ok("Payment processed successfully, Transaction ID: " + transactionId);
    }
}*/
