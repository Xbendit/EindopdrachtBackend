package com.ben.Backend_eindopdracht.controllers;


import com.ben.Backend_eindopdracht.dtos.UserOutputDto;
import com.ben.Backend_eindopdracht.dtos.WalletInputDto;
import com.ben.Backend_eindopdracht.dtos.WalletOutputDto;
import com.ben.Backend_eindopdracht.mappers.UserMapper;
import com.ben.Backend_eindopdracht.mappers.WalletMapper;
import com.ben.Backend_eindopdracht.models.Wallet;
import com.ben.Backend_eindopdracht.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<WalletOutputDto> createWallet(@RequestBody WalletInputDto input) {
        // DTO > Entity
        Wallet toSave = WalletMapper.toEntity(input);
        // Opslaan in database
        Wallet saved = walletService.save(toSave);
        // Entity > Output DTO
        WalletOutputDto output = WalletMapper.toOutputDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @GetMapping
    public ResponseEntity<List<WalletOutputDto>> getAllWallets(){
        List<Wallet> wallets = walletService.findAll();
        return ResponseEntity.ok(WalletMapper.toOutputDtoList(wallets));
    }
    @GetMapping("/{id}")
    public ResponseEntity<WalletOutputDto> getWalletbyId(@PathVariable Long id){
        return ResponseEntity.ok(WalletMapper.toOutputDto(this.walletService.getWallet(id)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<WalletOutputDto> updateWallet(@PathVariable Long id, @RequestBody WalletOutputDto walletOutputDto){
        WalletOutputDto updateWallet = this.walletService.updateWallet(id,walletOutputDto);
        return ResponseEntity.ok(updateWallet);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWalletById(@PathVariable Long id){
        String result = walletService.deleteWallet(id);
        return ResponseEntity.ok(result);
    }
}
