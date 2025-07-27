package com.ben.Backend_eindopdracht.repositories;

import com.ben.Backend_eindopdracht.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
}
