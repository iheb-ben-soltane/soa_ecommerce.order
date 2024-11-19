package tn.soa_ecommerce.order.mapper.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import tn.soa_ecommerce.order.dto.OrderItemDTO;
import tn.soa_ecommerce.order.mapper.Mapper;
import tn.soa_ecommerce.order.model.OrderItem;

@Component
@AllArgsConstructor
public class OrderItemMapperImpl implements Mapper<OrderItem, OrderItemDTO> {


    private final ModelMapper modelMapper;

    @Override
    public OrderItemDTO mapTo(OrderItem orderItem) {
        return modelMapper.map(orderItem, OrderItemDTO.class);

    }
    @Override
    public OrderItem mapFrom(OrderItemDTO orderItemDto) {
        return modelMapper.map(orderItemDto, OrderItem.class);
    }
}
