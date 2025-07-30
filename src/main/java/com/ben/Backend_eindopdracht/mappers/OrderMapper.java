package com.ben.Backend_eindopdracht.mappers;


import com.ben.Backend_eindopdracht.dtos.OrderInputDto;
import com.ben.Backend_eindopdracht.dtos.OrderOutputDto;

import com.ben.Backend_eindopdracht.models.Order;


public class OrderMapper {

    // van OrderinputDTO naar OrderEntity
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
}
