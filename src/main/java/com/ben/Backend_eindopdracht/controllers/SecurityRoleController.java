package com.ben.Backend_eindopdracht.controllers;

import com.ben.Backend_eindopdracht.dtos.SecurityRoleInputDto;
import com.ben.Backend_eindopdracht.dtos.SecurityRoleOutputDto;
import com.ben.Backend_eindopdracht.dtos.UserInputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.mappers.SecurityRoleMapper;
import com.ben.Backend_eindopdracht.models.SecurityRole;
import com.ben.Backend_eindopdracht.models.User;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import com.ben.Backend_eindopdracht.services.SecurityRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/securityroles")
@RequiredArgsConstructor
public class SecurityRoleController {

    private final SecurityRoleService securityRoleService;
    private final UserRepository userRepository;

    @PostMapping("/{userId}/assign")
    public ResponseEntity<SecurityRoleOutputDto> createrole(@PathVariable("userId") Long userId, @RequestBody SecurityRoleInputDto input){

        // Haal User op
        User user = userRepository.findById(userId).orElseThrow(()-> new RecordNotFoundException("User not found"));

        //SecurityRole toSave = SecurityRoleMapper.toEntity(input);
        SecurityRole securityRole = SecurityRoleMapper.toEntity(input);

        securityRole.setUsers(user);

        SecurityRole saved = securityRoleService.save(securityRole);

        SecurityRoleOutputDto output = SecurityRoleMapper.toOutputDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);

    }
    @PutMapping("/{id}")
    public ResponseEntity<SecurityRoleOutputDto> updateSecurityRole(@PathVariable Long id, @RequestBody SecurityRoleOutputDto securityRoleOutputDto){
        SecurityRoleOutputDto updateSecurityRole = this.securityRoleService.updateSecurityRole(id, securityRoleOutputDto);
        return ResponseEntity.ok(updateSecurityRole);
    }
}
