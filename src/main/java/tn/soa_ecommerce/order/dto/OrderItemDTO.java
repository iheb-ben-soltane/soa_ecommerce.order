package tn.soa_ecommerce.order.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class OrderItemDTO {
    private UUID productID;
    private Integer quantity;
}