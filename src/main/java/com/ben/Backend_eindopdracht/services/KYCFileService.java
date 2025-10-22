package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.dtos.KYCFileOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.mappers.KYCFileMapper;
import com.ben.Backend_eindopdracht.models.KYCFile;
import com.ben.Backend_eindopdracht.models.User;
import com.ben.Backend_eindopdracht.repositories.KYCFileRepository;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KYCFileService {

    private final KYCFileRepository kycFileRepository;
    private final UserRepository userRepository;

    @Value("${app.kyc.upload-dir:kyc-uploads}")
    private String uploadDir;

    public KYCFileOutputDto uploadPdf(Long userId, MultipartFile file) throws IOException {
        // 1) Validaties
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Geen bestand ontvangen.");
        }
        // Alleen PDF via content-type én extensie
        String contentType = file.getContentType();
        boolean isPdf = "application/pdf".equalsIgnoreCase(contentType)
                || (file.getOriginalFilename() != null && file.getOriginalFilename().toLowerCase().endsWith(".pdf"));
        if (!isPdf) {
            throw new IllegalArgumentException("Alleen PDF-bestanden zijn toegestaan.");
        }

        // 2) User ophalen
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User " + userId + " not found"));

        // 3) Doelmap + bestandsnaam
        Path dir = Path.of(uploadDir, "user-" + userId);
        Files.createDirectories(dir);

        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "upload.pdf";
        String safeName = original.replaceAll("[^a-zA-Z0-9._-]", "_");

        String unique = UUID.randomUUID() + "-" + Instant.now().toEpochMilli();
        String finalName = unique + "-" + safeName;

        Path target = dir.resolve(finalName);

        // 4) Wegschrijven
        Files.copy(file.getInputStream(), target);

        // 5) Entity opslaan
        KYCFile entity = new KYCFile();
        entity.setFileName(original);
        entity.setFilePath(target.toString());    // je kunt hier ook een relatieve path bewaren
        entity.setFileSize(file.getSize());
        entity.setUsers(user);

        KYCFile saved = kycFileRepository.save(entity);

        // 6) Teruggeven als DTO
        return KYCFileMapper.toOutputDto(saved);
    }

    /*-*/

   /* public KYCFileService(KYCFileRepository kycFileRepository) {
        this.kycFileRepository = kycFileRepository;
    }*/

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

    /*public String deleteKYCFile(long id) {
        if(!kycFileRepository.existsById(id)){
            throw new RecordNotFoundException("KYCFile " + id + " not found!");
        }
        kycFileRepository.deleteById(id);
        return "KYC File "+ id + " succesfully deleted";
    }*/

    @Transactional
    public String deleteKYCFile(long id) {
        KYCFile file = kycFileRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("KYCFile " + id + " not found!"));

        User user = file.getUsers();
        file.setUsers(null);
        if (user != null && user.getKycFile() == file) {
            user.setKycFile(null);
        }

        kycFileRepository.delete(file);
        return "KYC File " + id + " succesfully deleted";
    }


}
