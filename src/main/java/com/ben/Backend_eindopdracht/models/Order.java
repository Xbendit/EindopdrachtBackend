package com.ben.Backend_eindopdracht.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@Entity

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
    private String Status;
    // @Column(nullable = false)
    // private Long userID;

}
