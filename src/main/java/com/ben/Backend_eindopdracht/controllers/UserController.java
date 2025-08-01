package com.ben.Backend_eindopdracht.controllers;


import com.ben.Backend_eindopdracht.dtos.UserInputDto;
import com.ben.Backend_eindopdracht.dtos.UserOutputDto;
import com.ben.Backend_eindopdracht.mappers.UserMapper;
import com.ben.Backend_eindopdracht.models.User;
import com.ben.Backend_eindopdracht.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserOutputDto> createUser(@RequestBody UserInputDto input) {
        // 1) DTO → Entity
        User toSave = UserMapper.toEntity(input);
        // 2) opslaan
        User saved = userService.save(toSave);
        // 3) Entity → Output DTO
        UserOutputDto output = UserMapper.toOutputDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping
    public ResponseEntity<List<UserOutputDto>> getAllUsers(){
        List<User> users = userService.findAll();
        return ResponseEntity.ok(UserMapper.toOutputDtoList(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserOutputDto> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(UserMapper.toOutputDto(this.userService.getUser(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserOutputDto> updateUser(@PathVariable Long id, @RequestBody UserOutputDto userOutputDto){
        UserOutputDto updateUser = this.userService.updateUser(id, userOutputDto);
        return ResponseEntity.ok(updateUser);

    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable long id){
        String result = userService.deleteUser(id);
        return ResponseEntity.ok(result);
    }



}


