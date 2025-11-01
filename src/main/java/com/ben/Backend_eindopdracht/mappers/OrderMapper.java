package com.ben.Backend_eindopdracht.mappers;


import com.ben.Backend_eindopdracht.dtos.OrderInputDto;
import com.ben.Backend_eindopdracht.dtos.OrderOutputDto;
import com.ben.Backend_eindopdracht.models.Order;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static Order toEntity (OrderInputDto dto){
        Order o = new Order();
        o.setOrderType(dto.getOrderType());
        o.setAmount(dto.getAmount());
        o.setPrice(dto.getPrice());
        o.setCryptoCurrency(dto.getCryptoCurrency());
        o.setTimestamp(dto.getTimestamp());
        o.setStatus(dto.getStatus());
        return o;
    }

    public static OrderOutputDto toOutputDto (Order order){
        var p = new OrderOutputDto();
        p.setId(order.getId());
        p.setOrderType(order.getOrderType());
        p.setAmount(order.getAmount());
        p.setPrice(order.getPrice());
        p.setCryptoCurrency(order.getCryptoCurrency());
        p.setTimestamp(order.getTimestamp());
        p.setStatus(order.getStatus());
        return p;
    }

    public static List<OrderOutputDto> toOutputDtoList(List<Order> orders) {
        return orders.stream().map(OrderMapper::toOutputDto).collect(Collectors.toList());
    }
}
