package tn.soa_ecommerce.order.dto;

import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Getter
public class CartDTO {
    private UUID cartID;
    private UUID customerID;
    private Double totalAmount;
    private List<OrderItemDTO> items;
}
