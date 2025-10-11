package com.ben.Backend_eindopdracht.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityRoleOutputDto {

    private Long id;
    private String role;
}
