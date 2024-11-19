package tn.soa_ecommerce.order.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tn.soa_ecommerce.order.dto.OrderItemDTO;
import tn.soa_ecommerce.order.model.OrderItem;

import java.util.UUID;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Use strict matching for better field matching
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Custom mapping for UUID to handle potential edge cases
        modelMapper.addConverter(new Converter<String, UUID>() {
            public UUID convert(MappingContext<String, UUID> context) {
                return UUID.fromString(context.getSource());
            }
        });

        // Custom mapping for OrderItemDTO -> OrderItem
        modelMapper.addConverter(new Converter<OrderItemDTO, OrderItem>() {
            public OrderItem convert(MappingContext<OrderItemDTO, OrderItem> context) {
                OrderItemDTO source = context.getSource();
                OrderItem target = new OrderItem();
                target.setProductID(source.getProductID());
                target.setQuantity(source.getQuantity());
                return target;
            }
        });

        return modelMapper;
    }
}