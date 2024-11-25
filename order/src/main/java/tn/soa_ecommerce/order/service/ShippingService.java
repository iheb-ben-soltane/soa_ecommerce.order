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
public class ShippingService {

    private final RestTemplate restTemplate;

    public ShippingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean scheduleShipping(UUID orderId, UUID customerId) {
        String url = "http://localhost:8090/shipping/schedule";


        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", orderId);
        requestBody.put("customerId", customerId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && "scheduled".equalsIgnoreCase((String) responseBody.get("shippingStatus"))) {
            return true;
        }

        return false;
    }
}