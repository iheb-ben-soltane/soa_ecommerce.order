package tn.soa_ecommerce.order.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    @KafkaListener(topics = "my-topic", groupId = "my-group-id")
    public void listen(@Payload String message, Acknowledgment acknowledgment) {
        try {
            // Process the message
            System.out.println("Received message: " + message);

            // Simulate some processing time
            processMessage(message);

            // Manually acknowledge the message after successful processing
            acknowledgment.acknowledge();
        } catch (Exception e) {
            // Handle any processing errors
            System.err.println("Error processing message: " + e.getMessage());
            // You might want to handle failed messages differently,
            // such as sending to a dead letter queue
        }
    }

    private void processMessage(String message) throws InterruptedException {
        // Simulate message processing
        // In a real-world scenario, replace this with your actual message processing logic
        Thread.sleep(5000); // Simulate processing time
        System.out.println("Message processed: " + message);
    }
}