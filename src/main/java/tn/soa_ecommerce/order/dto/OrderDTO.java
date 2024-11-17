package tn.soa_ecommerce.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private UUID orderID;
    private UUID customerID;
    private Date orderDate;
    private Double totalAmount;
    private List<OrderItemDTO> items; // List of OrderItemDTO to represent items in the order
}