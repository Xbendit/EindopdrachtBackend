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

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Geen bestand ontvangen.");
        }

        String contentType = file.getContentType();
        boolean isPdf = "application/pdf".equalsIgnoreCase(contentType)
                || (file.getOriginalFilename() != null && file.getOriginalFilename().toLowerCase().endsWith(".pdf"));
        if (!isPdf) {
            throw new IllegalArgumentException("Alleen PDF-bestanden zijn toegestaan.");
        }


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User " + userId + " not found"));

        Path dir = Path.of(uploadDir, "user-" + userId);
        Files.createDirectories(dir);

        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "upload.pdf";
        String safeName = original.replaceAll("[^a-zA-Z0-9._-]", "_");

        String unique = UUID.randomUUID() + "-" + Instant.now().toEpochMilli();
        String finalName = unique + "-" + safeName;

        Path target = dir.resolve(finalName);

        Files.copy(file.getInputStream(), target);


        KYCFile entity = new KYCFile();
        entity.setFileName(original);
        entity.setFilePath(target.toString());    // je kunt hier ook een relatieve path bewaren
        entity.setFileSize(file.getSize());
        entity.setUsers(user);

        KYCFile saved = kycFileRepository.save(entity);

        return KYCFileMapper.toOutputDto(saved);
    }

    private KYCFile getRequired(Long id) {
        return kycFileRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("KYCFile " + id + " not found"));
    }

    public KYCFileOutputDto getKYCFileDto(Long id) {
        return KYCFileMapper.toOutputDto(getRequired(id));
    }

    @Transactional
    public KYCFileOutputDto updateStatus(Long id, KYCFile.KycFileStatus newStatus) {
        KYCFile k = kycFileRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("KYCFile " + id + " not found"));
        k.setFileStatus(newStatus);
        return KYCFileMapper.toOutputDto(k);
    }


    @Transactional
    public KYCFileOutputDto replacePdf(Long id, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Geen bestand ontvangen.");
        }
        boolean isPdf = "application/pdf".equalsIgnoreCase(file.getContentType())
                || (file.getOriginalFilename() != null && file.getOriginalFilename().toLowerCase().endsWith(".pdf"));
        if (!isPdf) throw new IllegalArgumentException("Alleen PDF-bestanden zijn toegestaan.");

        KYCFile k = getRequired(id);


        if (k.getFilePath() != null) {
            try { Files.deleteIfExists(Path.of(k.getFilePath())); } catch (Exception ignore) {}
        }


        Long userId = k.getUsers() != null ? k.getUsers().getId() : 0L;
        Path dir = Path.of(uploadDir, "user-" + userId);
        Files.createDirectories(dir);

        String original = file.getOriginalFilename() != null ? file.getOriginalFilename() : "upload.pdf";
        String safeName = original.replaceAll("[^a-zA-Z0-9._-]", "_");
        String finalName = UUID.randomUUID() + "-" + Instant.now().toEpochMilli() + "-" + safeName;

        Path target = dir.resolve(finalName);
        Files.copy(file.getInputStream(), target);

        k.setFileName(original);
        k.setFilePath(target.toString());
        k.setFileSize(file.getSize());

        KYCFile saved = kycFileRepository.save(k);
        return KYCFileMapper.toOutputDto(saved);
    }

    public org.springframework.core.io.Resource loadFileAsResource(Long id) {
        KYCFile k = getRequired(id);
        if (k.getFilePath() == null) throw new RecordNotFoundException("Geen bestandspad opgeslagen");
        Path p = Path.of(k.getFilePath());
        return new org.springframework.core.io.FileSystemResource(p);
    }

    @Transactional
    public String deleteKYCFile(long id) {
        KYCFile file = getRequired(id);

        User user = file.getUsers();
        file.setUsers(null);
        if (user != null && user.getKycFile() == file) {
            user.setKycFile(null);
        }

        if (file.getFilePath() != null) {
            try { Files.deleteIfExists(Path.of(file.getFilePath())); } catch (Exception ignore) {}
        }

        kycFileRepository.delete(file);
        return "KYC File " + id + " succesfully deleted";
    }












    /*private String uploadDir;

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

    *//*-*//*

   *//* public KYCFileService(KYCFileRepository kycFileRepository) {
        this.kycFileRepository = kycFileRepository;
    }*//*

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

    *//*public String deleteKYCFile(long id) {
        if(!kycFileRepository.existsById(id)){
            throw new RecordNotFoundException("KYCFile " + id + " not found!");
        }
        kycFileRepository.deleteById(id);
        return "KYC File "+ id + " succesfully deleted";
    }*//*

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
    }*/


}
