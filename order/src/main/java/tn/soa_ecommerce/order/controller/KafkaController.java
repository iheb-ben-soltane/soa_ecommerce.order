package tn.soa_ecommerce.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/send")
    public String sendMessage(@RequestParam("topic") String topic, @RequestParam("message") String message) {
        // Send message to Kafka topic
        kafkaTemplate.send(topic, message);
        return "Message sent to topic '" + topic + "': " + message;
    }
}