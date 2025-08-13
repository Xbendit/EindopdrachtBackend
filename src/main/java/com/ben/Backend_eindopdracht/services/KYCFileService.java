package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.dtos.KYCFileOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.mappers.KYCFileMapper;
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

    public KYCFile getKYCFile(Long id) {
        return this.kycFileRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("KYCFile " + id + " not found"));
    }

    public KYCFileOutputDto updateKYCFile(Long id, KYCFileOutputDto kycFileOutputDto) {
        KYCFile kycFile = kycFileRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("KYCFile " + id + " not found"));
        kycFile.setFileName(kycFileOutputDto.getFileName());
        kycFile.setFilePath(kycFileOutputDto.getFilePath());
        kycFile.setFileStatus(kycFileOutputDto.getFileStatus());

        KYCFile savedKYCFile = kycFileRepository.save(kycFile);

        return KYCFileMapper.toOutputDto(savedKYCFile);

    }

    public String deleteKYCFile(long id) {
        if(!kycFileRepository.existsById(id)){
            throw new RecordNotFoundException("KYCFile " + id + " not found!");
        }
        kycFileRepository.deleteById(id);
        return "User "+ id + " succesfully deleted";
    }
}
