package com.ben.Backend_eindopdracht.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Entity
@Getter
@Setter

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String orderType;
    @Column(nullable = false)
    private Long amount;
    @Column(nullable = false)
    private Long price;
    @Column(nullable = false)
    private String cryptoCurrency;
    @Column(nullable = false)
    private LocalDateTime timestamp;
    @Column(nullable = false)
    private String status;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User users;

    @ManyToOne(optional = false)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id", nullable = false)
    private Wallet wallets;
}
