package com.ben.Backend_eindopdracht.services;


import com.ben.Backend_eindopdracht.dtos.WalletOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.mappers.WalletMapper;
import com.ben.Backend_eindopdracht.models.Wallet;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import com.ben.Backend_eindopdracht.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public String deleteWallet(Long id) {
        Wallet w = walletRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Wallet " + id + " not found"));


        if (w.getOrders() != null && !w.getOrders().isEmpty()) {
            throw new RecordNotFoundException("Wallet " + id + " heeft nog orders. Verwijder/annuleer die eerst.");
        }


        if (w.getUsers() != null && w.getUsers().getWallets() != null) {
            w.getUsers().getWallets().remove(w);
        }

        walletRepository.delete(w); // delete(entity) i.p.v. deleteById
        return "Wallet " + id + " successfully deleted";
    }


}

