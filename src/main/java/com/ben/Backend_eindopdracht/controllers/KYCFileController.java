package com.ben.Backend_eindopdracht.controllers;

import com.ben.Backend_eindopdracht.dtos.KYCFileOutputDto;
import com.ben.Backend_eindopdracht.dtos.KYCStatusUpdateRequest;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import com.ben.Backend_eindopdracht.services.KYCFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/kycfiles")
@RequiredArgsConstructor
public class KYCFileController {

    private final KYCFileService kycFileService;
    private final UserRepository userRepository;

    @PostMapping(value = "/{userId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KYCFileOutputDto> upload(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file) throws Exception {

        KYCFileOutputDto dto = kycFileService.uploadPdf(userId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KYCFileOutputDto> getKYCFileById(@PathVariable Long id){
        return ResponseEntity.ok(kycFileService.getKYCFileDto(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<KYCFileOutputDto> updateStatus(
            @PathVariable Long id,
            @RequestBody KYCStatusUpdateRequest body) {
        return ResponseEntity.ok(kycFileService.updateStatus(id, body.getStatus()));
    }

    @PutMapping(value = "/{id}/replace", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<KYCFileOutputDto> replaceFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws Exception {
        KYCFileOutputDto dto = kycFileService.replacePdf(id, file);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable Long id) throws Exception {
        var resource = kycFileService.loadFileAsResource(id);  // retourneert org.springframework.core.io.Resource
        byte[] bytes = resource.getInputStream().readAllBytes();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(bytes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteKYCFileById(@PathVariable long id){
        String result = kycFileService.deleteKYCFile(id);
        return ResponseEntity.ok(result);
    }

}
