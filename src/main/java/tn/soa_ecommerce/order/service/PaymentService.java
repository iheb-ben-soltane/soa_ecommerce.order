package tn.soa_ecommerce.order.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final RestTemplate restTemplate;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean processPayment(String cartId, String userId, double amount) {
        String url = "http://localhost:8086/payment/process"; // URL of the Payment microservice

        // Prepare the request payload using a Map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cartId", cartId);   // Pass the cartId
        requestBody.put("userId", userId);   // Pass the userId
        requestBody.put("amount", amount);   // Pass the amount to be paid

        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Wrap the body and headers into an HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send POST request to Payment microservice and get the response
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        // Get the payment status from the response
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && "success".equalsIgnoreCase((String) responseBody.get("paymentStatus"))) {
            return true; // Payment was successful
        }

        return false; // Payment failed
    }
}