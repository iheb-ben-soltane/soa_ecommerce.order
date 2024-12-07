package tn.soa_ecommerce.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tn.soa_ecommerce.order.repository.OrderRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceToCompleteWorkflow {
    private final OrderService orderService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;

    // Kafka Topics
    private static final String INVENTORY_RESERVE_TOPIC = "inventory-reserve-request";
    private static final String PAYMENT_PROCESS_TOPIC = "payment-process-request";
    private static final String SHIPPING_SCHEDULE_TOPIC = "shipping-schedule-request";
    private static final String NOTIFICATION_SEND_TOPIC = "notification-send-request";

    private static final String INVENTORY_RESERVE_RESULT_TOPIC = "inventory-reserve-result";
    private static final String PAYMENT_PROCESS_RESULT_TOPIC = "payment-process-result";
    private static final String SHIPPING_SCHEDULE_RESULT_TOPIC = "shipping-schedule-result";
    private static final String NOTIFICATION_SEND_RESULT_TOPIC = "notification-send-result";

    public OrderServiceToCompleteWorkflow(OrderService orderService, KafkaTemplate<String, String> kafkaTemplate,
                                          ObjectMapper objectMapper, OrderRepository orderRepository) {
        this.orderService = orderService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.orderRepository = orderRepository;
    }


    public void sendKafkaMessage(String topic, String key, Map<String, Object> message) {
        try {
            String messageString = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topic, key, messageString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Kafka consumer for INVENTORY_RESERVE_TOPIC
    @KafkaListener(topics = INVENTORY_RESERVE_TOPIC, groupId = "order-workflow-group")
    public void consumeInventoryReserveRequest(String message) {
        try {
            Map<String, Object> request = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(request.get("orderId").toString());
            // Simulate inventory reserve logic here
            boolean inventorySuccess = true; // Simulate success or failure

            // After processing the request, send the result
            simulateInventoryReserveResult(orderId, inventorySuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void simulateInventoryReserveResult(UUID orderId, boolean success) {
        Map<String, Object> inventoryReserveResult = new HashMap<>();
        inventoryReserveResult.put("orderId", orderId.toString());
        inventoryReserveResult.put("success", success);

        // Send the result to the inventory-reserve-result topic
        sendKafkaMessage(INVENTORY_RESERVE_RESULT_TOPIC, orderId.toString(), inventoryReserveResult);
    }

    // Kafka consumer for PAYMENT_PROCESS_TOPIC
    @KafkaListener(topics = PAYMENT_PROCESS_TOPIC, groupId = "order-workflow-group")
    public void consumePaymentProcessRequest(String message) {
        try {
            Map<String, Object> request = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(request.get("orderId").toString());
            // Simulate payment processing logic here
            boolean paymentSuccess = true; // Simulate success or failure

            // After processing the request, send the result
            simulatePaymentProcessResult(orderId, paymentSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void simulatePaymentProcessResult(UUID orderId, boolean success) {
        Map<String, Object> paymentProcessResult = new HashMap<>();
        paymentProcessResult.put("orderId", orderId.toString());
        paymentProcessResult.put("success", success);

        // Send the result to the payment-process-result topic
        sendKafkaMessage(PAYMENT_PROCESS_RESULT_TOPIC, orderId.toString(), paymentProcessResult);
    }

    // Kafka consumer for SHIPPING_SCHEDULE_TOPIC
    @KafkaListener(topics = SHIPPING_SCHEDULE_TOPIC, groupId = "order-workflow-group")
    public void consumeShippingScheduleRequest(String message) {
        try {
            Map<String, Object> request = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(request.get("orderId").toString());
            // Simulate shipping scheduling logic here
            boolean shippingSuccess = true; // Simulate success or failure

            // After processing the request, send the result
            simulateShippingScheduleResult(orderId, shippingSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void simulateShippingScheduleResult(UUID orderId, boolean success) {
        Map<String, Object> shippingScheduleResult = new HashMap<>();
        shippingScheduleResult.put("orderId", orderId.toString());
        shippingScheduleResult.put("success", success);

        // Send the result to the shipping-schedule-result topic
        sendKafkaMessage(SHIPPING_SCHEDULE_RESULT_TOPIC, orderId.toString(), shippingScheduleResult);
    }

    // Kafka consumer for NOTIFICATION_SEND_TOPIC
    @KafkaListener(topics = NOTIFICATION_SEND_TOPIC, groupId = "order-workflow-group")
    public void consumeNotificationSendRequest(String message) {
        try {
            Map<String, Object> request = objectMapper.readValue(message, Map.class);
            UUID orderId = UUID.fromString(request.get("orderId").toString());
            // Simulate notification sending logic here
            boolean notificationSuccess = true; // Simulate success or failure

            // After processing the request, send the result
            simulateNotificationSendResult(orderId, notificationSuccess);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void simulateNotificationSendResult(UUID orderId, boolean success) {
        Map<String, Object> notificationSendResult = new HashMap<>();
        notificationSendResult.put("orderId", orderId.toString());
        notificationSendResult.put("success", success);

        // Send the result to the notification-send-result topic
        sendKafkaMessage(NOTIFICATION_SEND_RESULT_TOPIC, orderId.toString(), notificationSendResult);
    }
}