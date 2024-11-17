package tn.soa_ecommerce.order.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import tn.soa_ecommerce.order.model.OrderItem;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
public class InventoryService {

    private final RestTemplate restTemplate;

    public InventoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean reserveProducts(List<OrderItem> products, String orderId) {
        String url = "http://localhost:8085/inventory/reserve";

        // Prepare the request payload using a Map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", orderId); // Pass the orderId
        requestBody.put("products", products); // Pass the list of OrderItemDTO

        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Wrap the body and headers into an HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send POST request to Inventory microservice and get the response
        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, requestEntity, Boolean.class);

        // Return the response body which should be true or false based on inventory reservation
        return response.getBody() != null && response.getBody();
    }
}