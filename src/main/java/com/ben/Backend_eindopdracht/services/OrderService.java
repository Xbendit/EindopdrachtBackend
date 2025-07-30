package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.models.Order;
import com.ben.Backend_eindopdracht.repositories.OrderRepository;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;


    public OrderService(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
    }
    public Order save(Order order) {
        return orderRepository.save(order);
    }
}
