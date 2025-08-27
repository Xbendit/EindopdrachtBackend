package com.ben.Backend_eindopdracht.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserOutputDto {

    private Long id;
    private String username;
    private String email;
    /*private String role;*/

}
