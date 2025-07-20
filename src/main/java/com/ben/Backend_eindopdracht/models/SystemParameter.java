package com.ben.Backend_eindopdracht.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="systeemparameters")
@Entity
public class SystemParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String parametername;
    @Column(nullable = false)
    private Long value;
    @Column(nullable = false)
    private String lastAdjustedBy;
    @Column(nullable = false)
    private LocalDateTime lastAdjustedOn;

}
