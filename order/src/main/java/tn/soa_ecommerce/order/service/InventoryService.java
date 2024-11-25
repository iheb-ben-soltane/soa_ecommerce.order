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
import java.util.UUID;

@Service
public class InventoryService {

    private final RestTemplate restTemplate;

    public InventoryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean reserveProducts(UUID orderId, List<OrderItem> products) {
        String url = "http://localhost:8085/inventory/reserve";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", orderId);
        requestBody.put("products", products);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, requestEntity, Boolean.class);

        return response.getBody() != null && response.getBody();
    }
}