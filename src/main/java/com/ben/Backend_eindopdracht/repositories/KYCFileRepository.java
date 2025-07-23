package com.ben.Backend_eindopdracht.repositories;


import com.ben.Backend_eindopdracht.models.KYCFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KYCFileRepository extends JpaRepository <KYCFile,Long> {
}
