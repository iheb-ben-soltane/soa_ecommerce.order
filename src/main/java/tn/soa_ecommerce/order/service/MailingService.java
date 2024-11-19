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
public class MailingService {

    private final RestTemplate restTemplate;

    public MailingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean sendEmail(UUID orderId, UUID customerId, String orderStatus, double amount) {
        String url = "http://localhost:8091/mail/send"; // URL of the Mailing microservice

        // Prepare the request payload using a Map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("templateId", "orderConfirmationTemplate");
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderId", orderId);
        variables.put("customerId", customerId);
        variables.put("orderStatus", orderStatus);
        variables.put("amount", amount);
        requestBody.put("variables", variables);

        // Set headers for the request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Wrap the body and headers into an HttpEntity
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send POST request to Mailing microservice and get the response
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);

        // Get the email status from the response
        Map<String, Object> responseBody = response.getBody();
        if (responseBody != null && "sent".equalsIgnoreCase((String) responseBody.get("emailStatus"))) {
            return true; // Email was successfully sent
        }

        return false; // Email sending failed
    }
}