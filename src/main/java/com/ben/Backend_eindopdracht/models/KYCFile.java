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
    private String filename;
    @Column(nullable = false)
    private String filePath;
    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    //@ManyToOne
    //@JoinColumn(name = "user_id", nullable = false)
    //private User user;

}
