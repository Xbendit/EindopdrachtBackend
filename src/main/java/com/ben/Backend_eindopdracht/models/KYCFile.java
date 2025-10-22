package com.ben.Backend_eindopdracht.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="kycfile")
@Entity
@Getter
@Setter




public class KYCFile {
    /*-*/
    public enum KycFileStatus {
        PENDING, APPROVED, REJECTED
    }
    /*-*/


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    /*- private String fileStatus;*/

    /*-*/
    @Enumerated(EnumType.STRING)
    @Column(name = "file_status", nullable = false)
    private KycFileStatus fileStatus = KycFileStatus.PENDING;

    @Column(name = "file_size_bytes", nullable = false)
    private long fileSize;
    /*-*/

    /*- @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id",nullable = false)
    private User users;*/

    /*-*/
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User users;
    /*-*/

}
