package com.ben.Backend_eindopdracht.dtos;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletOutputDto {

    private Long id;
    private String walletAdress;
    private String cryptoCurrency;
    private Long balance;
    // private Long userId;
}
