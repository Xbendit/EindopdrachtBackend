package com.ben.Backend_eindopdracht.controllers;


import com.ben.Backend_eindopdracht.dtos.OrderInputDto;
import com.ben.Backend_eindopdracht.dtos.OrderOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.mappers.OrderMapper;
import com.ben.Backend_eindopdracht.models.Order;
import com.ben.Backend_eindopdracht.models.User;
import com.ben.Backend_eindopdracht.models.Wallet;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import com.ben.Backend_eindopdracht.repositories.WalletRepository;
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
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @PostMapping("/{userId}/{walletId}/assign")
    public ResponseEntity<OrderOutputDto> createUser(@PathVariable("userId") Long userId, @PathVariable("walletId") Long walletId, @RequestBody OrderInputDto input){

        // Haal User op
        User user = userRepository.findById(userId).orElseThrow(()-> new RecordNotFoundException("User not found"));
        // Haal User op
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(()-> new RecordNotFoundException("Wallet not found"));

        //DTO > Entity
        Order order = OrderMapper.toEntity(input);
        //Koppel User
        order.setUsers(user);
        order.setWallets(wallet);

        // Oplaan in database
        Order saved = orderService.save(order);
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
