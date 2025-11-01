package com.ben.Backend_eindopdracht.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallets")
@Entity
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String walletAdress;
    @Column(nullable = false)
    private String cryptoCurrency;
    @Column(nullable = false)
    private Long balance;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User users;

    @OneToMany(mappedBy = "wallets", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;

}
