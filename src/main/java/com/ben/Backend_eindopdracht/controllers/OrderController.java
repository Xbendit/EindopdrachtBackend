package com.ben.Backend_eindopdracht.controllers;


import com.ben.Backend_eindopdracht.dtos.OrderInputDto;
import com.ben.Backend_eindopdracht.dtos.OrderOutputDto;
import com.ben.Backend_eindopdracht.dtos.UserOutputDto;
import com.ben.Backend_eindopdracht.mappers.OrderMapper;
import com.ben.Backend_eindopdracht.models.Order;
import com.ben.Backend_eindopdracht.services.OrderService;
import com.ben.Backend_eindopdracht.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

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

    @GetMapping
    public ResponseEntity<List<OrderOutputDto>> getAllOrders(){
        List<Order> orders = orderService.findAll();
        return ResponseEntity.ok(OrderMapper.toOutputDtoList(orders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderOutputDto> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(OrderMapper.toOutputDto(this.orderService.getOrder(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderOutputDto> updateOrder(@PathVariable Long id, @RequestBody OrderOutputDto orderOutputDto){
        OrderOutputDto updateOrder = this.orderService.updateOrder(id, orderOutputDto);
        return ResponseEntity.ok(updateOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <String> deleteById(@PathVariable long id){
        String result = orderService.deleteUser(id);
        return ResponseEntity.ok(result);
    }


}
