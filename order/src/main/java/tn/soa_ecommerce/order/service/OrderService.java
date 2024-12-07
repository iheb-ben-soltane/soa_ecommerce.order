package tn.soa_ecommerce.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import tn.soa_ecommerce.order.dto.OrderDTO;
import tn.soa_ecommerce.order.mapper.Mapper;
import tn.soa_ecommerce.order.model.Order;
import tn.soa_ecommerce.order.model.OrderStatus;
import tn.soa_ecommerce.order.producer.KafkaMessageProducer;
import tn.soa_ecommerce.order.repository.OrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final Mapper<Order, OrderDTO> orderMapper;
    private final KafkaMessageProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    // Kafka Topics
    private static final String INVENTORY_RESERVE_TOPIC = "inventory-reserve-request";
    private static final String PAYMENT_PROCESS_TOPIC = "payment-process-request";
    private static final String SHIPPING_SCHEDULE_TOPIC = "shipping-schedule-request";
    private static final String NOTIFICATION_SEND_TOPIC = "notification-send-request";

    private static final String INVENTORY_RESERVE_RESULT_TOPIC = "inventory-reserve-result";
    private static final String PAYMENT_PROCESS_RESULT_TOPIC = "payment-process-result";
    private static final String SHIPPING_SCHEDULE_RESULT_TOPIC = "shipping-schedule-result";
    private static final String NOTIFICATION_SEND_RESULT_TOPIC = "notification-send-result";

    public OrderService(
            OrderRepository orderRepository,
            Mapper<Order, OrderDTO> orderMapper,
            KafkaMessageProducer kafkaProducer,
            ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    public OrderDTO createOrder(Order order) throws JsonProcessingException {
        try {
            // Initialize order
            order.setStatus(OrderStatus.CREATED);
            Order savedOrder = orderRepository.save(order);
            System.out.println(objectMapper.writeValueAsString(savedOrder));
            Map<String, Object> inventoryDetails = new HashMap<>();
            inventoryDetails.put("orderId", savedOrder.getOrderID());
            inventoryDetails.put("Items", order.getItems());

            // Trigger inventory reservation
            kafkaProducer.sendMessage(
                    INVENTORY_RESERVE_TOPIC,
                    savedOrder.getOrderID().toString(),
                    inventoryDetails
            );

            return orderMapper.mapTo(savedOrder);

        } catch (Exception e) {
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            throw e;
        }
    }

    @KafkaListener(topics = INVENTORY_RESERVE_RESULT_TOPIC, groupId = "order-workflow-group")
    public void handleInventoryReservationResult(String message, Acknowledgment acknowledgment) {
        try {
            Map<String, Object> result = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(result.get("orderId").toString());
            boolean success = (boolean) result.get("success");
            if (success) {
                updateOrderStatus(orderId, OrderStatus.RESERVED);
                // Prepare payment details (orderId, customerId, totalAmount)
                Optional<Order> orderOptional = getOrderById(orderId);
                if (orderOptional.isPresent()) {
                    Order order = orderOptional.get();
                    System.out.println(objectMapper.writeValueAsString(order));


                    Map<String, Object> paymentDetails = new HashMap<>();
                    paymentDetails.put("orderId", order.getOrderID());
                    paymentDetails.put("customerId", order.getCustomerID());
                    paymentDetails.put("totalAmount", order.getTotalAmount());

                    // Trigger payment processing
                    kafkaProducer.sendMessage(
                            PAYMENT_PROCESS_TOPIC,
                            orderId.toString(),
                            paymentDetails
                    );
                }
            } else {
                // Inventory reservation failed
                updateOrderStatus(orderId, OrderStatus.FAILED);
            }


            acknowledgment.acknowledge();
        } catch (Exception e) {
            // Handle parsing or processing errors
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = PAYMENT_PROCESS_RESULT_TOPIC, groupId = "order-workflow-group")
    public void handlePaymentProcessResult(String message, Acknowledgment acknowledgment) {
        try {
            Map<String, Object> result = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(result.get("orderId").toString());
            boolean success = (boolean) result.get("success");

            if (success) {
                updateOrderStatus(orderId, OrderStatus.PAID);
                // Prepare payment details (orderId, customerId, totalAmount)
                Optional<Order> orderOptional = getOrderById(orderId);
                if (orderOptional.isPresent()) {
                    Order order = orderOptional.get();
                    System.out.println(objectMapper.writeValueAsString(order));


                    Map<String, Object> shippingDetails = new HashMap<>();
                    shippingDetails.put("orderId", order.getOrderID());
                    shippingDetails.put("customerId", order.getCustomerID());

                    // Trigger shipping scheduling
                    kafkaProducer.sendMessage(
                            SHIPPING_SCHEDULE_TOPIC,
                            orderId.toString(),
                            shippingDetails
                    );
                }
            } else {
                // Payment processing failed
                updateOrderStatus(orderId, OrderStatus.FAILED);
            }

            acknowledgment.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = SHIPPING_SCHEDULE_RESULT_TOPIC, groupId = "order-workflow-group")
    public void handleShippingScheduleResult(String message, Acknowledgment acknowledgment) {
        try {
            Map<String, Object> result = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(result.get("orderId").toString());
            boolean success = (boolean) result.get("success");

            if (success) {
                updateOrderStatus(orderId, OrderStatus.SHIPPING_SCHEDULED);
                // Prepare shipping details (orderId and customerId)
                Optional<Order> orderOptional = getOrderById(orderId);
                if (orderOptional.isPresent()) {
                    Order order = orderOptional.get();
                    System.out.println(objectMapper.writeValueAsString(order));
                    Map<String, Object> mailingDetails = new HashMap<>();
                    mailingDetails.put("orderId", order.getOrderID());
                    mailingDetails.put("customerId", order.getCustomerID());

                    // Trigger shipping scheduling
                    kafkaProducer.sendMessage(
                            NOTIFICATION_SEND_TOPIC,
                            orderId.toString(),
                            mailingDetails
                    );
                }
            } else {
                // Payment processing failed
               updateOrderStatus(orderId, OrderStatus.FAILED);
            }

            acknowledgment.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = NOTIFICATION_SEND_RESULT_TOPIC, groupId = "order-workflow-group")
    public void handleNotificationSendResult(String message, Acknowledgment acknowledgment) {
        try {
            Map<String, Object> result = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(result.get("orderId").toString());
            boolean success = (boolean) result.get("success");

            if (success) {
                updateOrderStatus(orderId, OrderStatus.COMPLETED);
                Optional<Order> orderOptional = getOrderById(orderId);
                if (orderOptional.isPresent()) {
                    Order order = orderOptional.get();
                    System.out.println(objectMapper.writeValueAsString(order));
                }

            } else {
                // Payment processing failed
                updateOrderStatus(orderId, OrderStatus.FAILED);
            }

            acknowledgment.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<Order> getOrderById(UUID id) {
        return orderRepository.findById(id);
    }

    public void updateOrderStatus(UUID orderId, OrderStatus status) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(status);
            orderRepository.save(order);
        }
    }
}