package com.ben.Backend_eindopdracht.mappers;

import com.ben.Backend_eindopdracht.dtos.WalletInputDto;
import com.ben.Backend_eindopdracht.dtos.WalletOutputDto;
import com.ben.Backend_eindopdracht.models.Wallet;

import java.util.List;
import java.util.stream.Collectors;


public class WalletMapper {

    //van WalletInputDTO naar WalletEntity
    public static Wallet toEntity (WalletInputDto dto){
        Wallet w = new Wallet();
        w.setWalletAdress(dto.getWalletAdress());
        w.setBalance(dto.getBalance());
        w.setCryptoCurrency(dto.getCryptoCurrency());
        return w;
    }

    // van WalletEntity to WalletOutputDto
    public static WalletOutputDto toOutputDto (Wallet wallet){
        var o = new WalletOutputDto();
        o.setId(wallet.getId());
        o.setWalletAdress(wallet.getWalletAdress());
        o.setBalance(wallet.getBalance());
        o.setCryptoCurrency(wallet.getCryptoCurrency());
        return o;
    }

    //lijst maken
    public static List<WalletOutputDto> toOutputDtoList (List<Wallet> wallets){
        return wallets.stream().map(WalletMapper::toOutputDto).collect(Collectors.toList());
    }

}

