package tn.soa_ecommerce.order.service;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    private final RestTemplate restTemplate;

    public PaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean processPayment(UUID orderId, UUID customerId, double amount) {
        String url = "http://localhost:8086/payment/process";


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", orderId);
        requestBody.put("customerId", customerId);
        requestBody.put("amount", amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && "success".equalsIgnoreCase((String) responseBody.get("paymentStatus"))) {
            return true;
        }

        return false;
    }
}