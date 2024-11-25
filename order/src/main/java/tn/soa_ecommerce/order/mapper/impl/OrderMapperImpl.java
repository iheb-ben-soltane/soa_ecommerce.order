package tn.soa_ecommerce.order.mapper.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import tn.soa_ecommerce.order.dto.OrderDTO;
import tn.soa_ecommerce.order.mapper.Mapper;
import tn.soa_ecommerce.order.model.Order;

@Component
@AllArgsConstructor
public class OrderMapperImpl implements Mapper<Order, OrderDTO> {
    private final ModelMapper modelMapper;
    @Override
    public OrderDTO mapTo(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public Order mapFrom(OrderDTO orderDTO) {

        return modelMapper.map(orderDTO, Order.class);
    }
}
