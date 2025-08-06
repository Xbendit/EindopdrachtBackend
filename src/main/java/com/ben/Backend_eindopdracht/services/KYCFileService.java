package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.models.KYCFile;
import com.ben.Backend_eindopdracht.repositories.KYCFileRepository;
import org.springframework.stereotype.Service;

@Service
public class KYCFileService {

    private final KYCFileRepository kycFileRepository;

    public KYCFileService(KYCFileRepository kycFileRepository) {
        this.kycFileRepository = kycFileRepository;
    }

    public KYCFile save(KYCFile kycFile) {
        return kycFileRepository.save(kycFile);
    }
}
