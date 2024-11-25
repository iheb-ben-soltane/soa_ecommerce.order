package tn.soa_ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.soa_ecommerce.order.model.OrderStatus;

import java.util.List;
import java.util.UUID;

@Data
public class OrderDTO {
    private UUID orderID;
    private UUID customerID;
    private UUID cartID;
    private Double totalAmount;
    private List<OrderItemDTO> items;
    private OrderStatus status;
}