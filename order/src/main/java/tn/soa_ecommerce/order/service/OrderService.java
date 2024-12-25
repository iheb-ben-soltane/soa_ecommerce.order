package tn.soa_ecommerce.order.service;

import org.springframework.stereotype.Service;
import tn.soa_ecommerce.order.dto.OrderDTO;
import tn.soa_ecommerce.order.dto.OrderItemDTO;
import tn.soa_ecommerce.order.mapper.Mapper;
import tn.soa_ecommerce.order.model.Order;
import tn.soa_ecommerce.order.model.OrderItem;
import tn.soa_ecommerce.order.model.OrderStatus;
import tn.soa_ecommerce.order.repository.OrderRepository;

import java.util.*;

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

        try {
           order = orderRepository.save(order);

           // Step 1: Reserve products in the inventory
            boolean isReserved = inventoryService.reserveProducts(order.getOrderID(), order.getItems());
            if (!isReserved) {
                order.setStatus(OrderStatus.FAILED);
                throw new RuntimeException("Failed to reserve products in inventory");
            }
            order.setStatus(OrderStatus.RESERVED);

 /*          // Step 2: Process payment
            boolean isPaymentSuccessful = paymentService.processPayment(order.getOrderID(), order.getCustomerID(), order.getTotalAmount());
            if (!isPaymentSuccessful) {
                order.setStatus(OrderStatus.FAILED);
                throw new RuntimeException("Payment failed");
            }
            order.setStatus(OrderStatus.PAID);

/*            // Step 3: Schedule shipping
            boolean isShippingScheduled = shippingService.scheduleShipping(order.getOrderID(), order.getCustomerID());
            if (!isShippingScheduled) {
                order.setStatus(OrderStatus.FAILED);
                throw new RuntimeException("Failed to schedule shipping");
            }
            order.setStatus(OrderStatus.SHIPPING_SCHEDULED);

            // Step 4: Send email notification
            boolean isEmailSent = mailingService.sendEmail(order.getOrderID(), order.getCustomerID(), order.getStatus(), order.getTotalAmount());
            if (!isEmailSent) {
                order.setStatus(OrderStatus.FAILED);
                throw new RuntimeException("Failed to send confirmation email");
            }*/

            order.setStatus(OrderStatus.COMPLETED);
            Order savedOrder = orderRepository.save(order);

            return orderMapper.mapTo(savedOrder);

        } catch (RuntimeException e) {
            // Persist the order status as FAILED if an exception occurs
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            throw e;
        }
    }

    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public boolean cancelOrder(UUID id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getStatus() != OrderStatus.COMPLETED) {
                order.setStatus(OrderStatus.CANCELED);
                orderRepository.save(order);
                return true;
            }
        }
        return false;
    }
}