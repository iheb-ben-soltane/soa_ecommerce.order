package tn.soa_ecommerce.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.soa_ecommerce.order.dto.CartDTO;
import tn.soa_ecommerce.order.dto.OrderDTO;
import tn.soa_ecommerce.order.dto.OrderItemDTO;
import tn.soa_ecommerce.order.mapper.Mapper;
import tn.soa_ecommerce.order.model.Order;
import tn.soa_ecommerce.order.model.OrderItem;
import tn.soa_ecommerce.order.service.OrderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final Mapper<OrderItem, OrderItemDTO> orderItemMapper;

    // Inject the OrderService to handle business logic
    public OrderController(OrderService orderService, Mapper<OrderItem, OrderItemDTO> orderItemMapper) {
        this.orderService = orderService;
        this.orderItemMapper = orderItemMapper;
    }

    // Endpoint to create an order
    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody CartDTO request) {
        try {
            // Log the incoming request for debugging
            // Convert OrderItemDTO to OrderItem model entities
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemDTO product : request.getItems()) {
                // Handle product validation and mapping
                orderItems.add(orderItemMapper.mapFrom(product));
            }

            // Map CartDTO to Order entity fields
            Order order = new Order();
            order.setCustomerID(request.getCustomerID());
            order.setCartID(request.getCartID());
            order.setTotalAmount(request.getTotalAmount());
            order.setItems(orderItems);

            // Associate each OrderItem with the current Order
            for (OrderItem item : orderItems) {
                item.setOrder(order);
            }

            OrderDTO createdOrder = orderService.createOrder(order);

            // Return the created order as response
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Handle validation errors (e.g., productID is null)
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle other generic errors
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}