package tn.soa_ecommerce.order.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.soa_ecommerce.order.dto.OrderDTO;
import tn.soa_ecommerce.order.dto.OrderItemDTO;
import tn.soa_ecommerce.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    // Inject the OrderService to handle business logic
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Endpoint to create an order
    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequest request) {
        try {
            // Call service method to create the order
            OrderDTO orderDTO = orderService.createOrder(
                    request.getUserId(),
                    request.getTotalPrice(),
                    request.getProducts()
            );

            // Return the created order as response
            return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle any exceptions and return a 400 Bad Request
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    // Inner DTO for the request body
    @Getter
    public static class OrderRequest {
        // Getters and Setters
        private UUID userId;
        private double totalPrice;
        private List<OrderItemDTO> products;

    }
}