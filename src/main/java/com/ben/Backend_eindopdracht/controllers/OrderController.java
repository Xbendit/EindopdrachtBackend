package com.ben.Backend_eindopdracht.controllers;


import com.ben.Backend_eindopdracht.dtos.OrderInputDto;
import com.ben.Backend_eindopdracht.dtos.OrderOutputDto;
import com.ben.Backend_eindopdracht.mappers.OrderMapper;
import com.ben.Backend_eindopdracht.models.Order;
import com.ben.Backend_eindopdracht.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderOutputDto> createUser(@RequestBody OrderInputDto input){
        //DTO > Entity
        Order toSave = OrderMapper.toEntity(input);
        // Oplaan in database
        Order saved = orderService.save(toSave);
        // Entity > OutputDTO
        OrderOutputDto output = OrderMapper.toOutputDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}
