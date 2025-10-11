package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.dtos.OrderOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.mappers.OrderMapper;
import com.ben.Backend_eindopdracht.models.Order;
import com.ben.Backend_eindopdracht.repositories.OrderRepository;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;


    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    public Order save(Order order) {
        return orderRepository.save(order);
    }
    public List<Order> findAll(){
        return orderRepository.findAll();
    }

    public Order getOrder(Long id) {
        return this.orderRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Order "+ id + " not found"));
    }

    public OrderOutputDto updateOrder(Long id, OrderOutputDto orderOutputDto) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Order " + id + " not found"));
        order.setOrderType(orderOutputDto.getOrderType());
        order.setAmount(orderOutputDto.getAmount());
        order.setPrice(orderOutputDto.getPrice());
        order.setCryptoCurrency(orderOutputDto.getCryptoCurrency());
        order.setTimestamp(orderOutputDto.getTimestamp());
        order.setStatus(orderOutputDto.getStatus());

        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toOutputDto(savedOrder);

    }

    public String deleteUser(long id) {
        if(!orderRepository.existsById(id)){
            throw new RecordNotFoundException("Order "+ id + " not found!");
        }
        orderRepository.deleteById(id);
        return "Order "+ id + " successfully deleted";
    }
}
