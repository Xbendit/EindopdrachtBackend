package com.ben.Backend_eindopdracht.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="kycfile")
@Entity

// public enum DocumentStatus {
   // PENDING,
    // APPROVED,
    // REJECTED
//}



public class KYCFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fileName;
    @Column(nullable = false)
    private String filePath;
    // @Enumerated(EnumType.STRING)
    private String fileStatus;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User users;

    //@ManyToOne
    //@JoinColumn(name = "user_id", nullable = false)
    //private User user;

}
