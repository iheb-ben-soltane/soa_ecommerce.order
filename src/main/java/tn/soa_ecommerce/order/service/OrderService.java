package tn.soa_ecommerce.order.service;

import org.springframework.stereotype.Service;
import tn.soa_ecommerce.order.dto.OrderDTO;
import tn.soa_ecommerce.order.dto.OrderItemDTO;
import tn.soa_ecommerce.order.mapper.Mapper;
import tn.soa_ecommerce.order.model.Order;
import tn.soa_ecommerce.order.model.OrderItem;
import tn.soa_ecommerce.order.repository.OrderRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final Mapper<Order, OrderDTO> orderMapper;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final MailingService mailingService;

    public OrderService(
            OrderRepository orderRepository,
            Mapper<Order, OrderDTO> orderMapper,
            InventoryService inventoryService,
            PaymentService paymentService,
            ShippingService shippingService,
            MailingService mailingService) {

        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.shippingService = shippingService;
        this.mailingService = mailingService;
    }

    public OrderDTO createOrder(Order order) {

  /*     // Step 1: Reserve products in the inventory
        boolean isReserved = inventoryService.reserveProducts( order.getOrderID(),order.getItems());
        System.out.println("isReserved: " +isReserved );

        if (!isReserved) {
            throw new RuntimeException("Failed to reserve products in inventory");
        }

      // Step 2: Process payment
        boolean isPaymentSuccessful = paymentService.processPayment(order.getOrderID(), order.getCustomerID(), order.getTotalAmount());
        if (!isPaymentSuccessful) {
            throw new RuntimeException("Payment failed");
        }

        // Step 3: Schedule shipping
        boolean isShippingScheduled = shippingService.scheduleShipping(order.getOrderID(), order.getCustomerID(), "Customer Address Here");
        if (!isShippingScheduled) {
            throw new RuntimeException("Failed to schedule shipping");
        }

        // Step 4: Send email notification
        boolean isEmailSent = mailingService.sendEmail(order.getOrderID(), order.getCustomerID(), "Order Confirmed", order.getTotalAmount());
        if (!isEmailSent) {
            throw new RuntimeException("Failed to send confirmation email");
        }
*/
        // Step 5: Save the order to the database if all steps were successful
        Order savedOrder = orderRepository.save(order);

        // Map saved Order entity back to OrderDTO to return
        return orderMapper.mapTo(savedOrder);
    }
}