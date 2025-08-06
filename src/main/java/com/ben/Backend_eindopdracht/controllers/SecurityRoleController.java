package com.ben.Backend_eindopdracht.controllers;

import com.ben.Backend_eindopdracht.dtos.SecurityRoleInputDto;
import com.ben.Backend_eindopdracht.dtos.SecurityRoleOutputDto;
import com.ben.Backend_eindopdracht.dtos.UserInputDto;
import com.ben.Backend_eindopdracht.mappers.SecurityRoleMapper;
import com.ben.Backend_eindopdracht.models.SecurityRole;
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

    @PostMapping
    public ResponseEntity<SecurityRoleOutputDto> createrole(@RequestBody SecurityRoleInputDto input){

        SecurityRole toSave = SecurityRoleMapper.toEntity(input);

        SecurityRole saved = securityRoleService.save(toSave);

        SecurityRoleOutputDto output = SecurityRoleMapper.toOutputDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);

    }
    @PutMapping("/{id}")
    public ResponseEntity<SecurityRoleOutputDto> updateSecurityRole(@PathVariable Long id, @RequestBody SecurityRoleOutputDto securityRoleOutputDto){
        SecurityRoleOutputDto updateSecurityRole = this.securityRoleService.updateSecurityRole(id, securityRoleOutputDto);
        return ResponseEntity.ok(updateSecurityRole);
    }
}
