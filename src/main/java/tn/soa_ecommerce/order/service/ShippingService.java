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
public class ShippingService {

    private final RestTemplate restTemplate;

    public ShippingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean scheduleShipping(String cartId, String userId, String address) {
        String url = "http://localhost:8090/shipping/schedule"; // URL of the Shipping microservice

        // Prepare the request payload using a Map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cartId", cartId);    // Pass the cartId
        requestBody.put("userId", userId);    // Pass the userId
        requestBody.put("address", address);  // Pass the shipping address

        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Wrap the body and headers into an HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send POST request to Shipping microservice and get the response
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        // Get the shipping status from the response
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && "scheduled".equalsIgnoreCase((String) responseBody.get("shippingStatus"))) {
            return true; // Shipping was successfully scheduled
        }

        return false; // Shipping failed
    }
}