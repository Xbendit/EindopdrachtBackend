package com.ben.Backend_eindopdracht.services;


import com.ben.Backend_eindopdracht.dtos.WalletOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.mappers.WalletMapper;
import com.ben.Backend_eindopdracht.models.Wallet;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import com.ben.Backend_eindopdracht.repositories.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;


    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;

    }

    public Wallet save(Wallet wallet){
        return walletRepository.save(wallet);
    }
    public List<Wallet> findAll(){
        return walletRepository.findAll();
    }
    public Wallet getWallet(Long id){
        return this.walletRepository.findById(id).orElseThrow(()->new RecordNotFoundException("Wallet "+ id + " not found"));
    }

    public WalletOutputDto updateWallet(Long id, WalletOutputDto walletOutputDto) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("Wallet "+ id+ " not found"));
        wallet.setWalletAdress(walletOutputDto.getWalletAdress());
        wallet.setBalance(walletOutputDto.getBalance());
        wallet.setCryptoCurrency(walletOutputDto.getCryptoCurrency());

        Wallet savedWallet = walletRepository.save(wallet);

        return WalletMapper.toOutputDto(savedWallet);

    }

    public String deleteWallet(Long id) {
        Wallet wallet = walletRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("Wallet "+ id+ " not found"));
        walletRepository.deleteById(id);
        return "Wallet "+ id + " successfully deleted";
    }
}
