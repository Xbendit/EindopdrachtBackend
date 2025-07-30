package com.ben.Backend_eindopdracht.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInputDto {

    private String orderType;
    private Long amount;
    private Long price;
    private String cryptoCurrency;
    private LocalDateTime timestamp;
    private String status;
    // private Long userID;
}
