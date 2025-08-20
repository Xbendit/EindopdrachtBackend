package com.ben.Backend_eindopdracht.controllers;

import com.ben.Backend_eindopdracht.dtos.KYCFileInputDto;
import com.ben.Backend_eindopdracht.dtos.KYCFileOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.mappers.KYCFileMapper;
import com.ben.Backend_eindopdracht.mappers.WalletMapper;
import com.ben.Backend_eindopdracht.models.KYCFile;
import com.ben.Backend_eindopdracht.models.User;
import com.ben.Backend_eindopdracht.models.Wallet;
import com.ben.Backend_eindopdracht.repositories.UserRepository;
import com.ben.Backend_eindopdracht.services.KYCFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kycfiles")
@RequiredArgsConstructor
public class KYCFileController {

    private final KYCFileService kycFileService;
    private final UserRepository userRepository;

    @PostMapping("/{userId}/assign")
    public ResponseEntity<KYCFileOutputDto> createKYCFile(@PathVariable("userId") Long userId, @RequestBody KYCFileInputDto input){

        // Haal User op
        User user = userRepository.findById(userId).orElseThrow(()-> new RecordNotFoundException("User not found"));

        //KYCFile toSave = KYCFileMapper.toEntity(input);
        KYCFile kycFile = KYCFileMapper.toEntity(input);

        //Koppel user
        kycFile.setUsers(user);

        KYCFile saved = kycFileService.save(kycFile);

        KYCFileOutputDto output = KYCFileMapper.toOutputDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    /*@PostMapping
    public ResponseEntity<KYCFileOutputDto> createKYCFile(@RequestBody KYCFileInputDto input){

        KYCFile toSave = KYCFileMapper.toEntity(input);

        KYCFile saved = kycFileService.save(toSave);

        KYCFileOutputDto output = KYCFileMapper.toOutputDto(saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<KYCFileOutputDto> getKYCFileById(@PathVariable Long id){
        return ResponseEntity.ok(KYCFileMapper.toOutputDto(this.kycFileService.getKYCFile(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<KYCFileOutputDto> updateKYCFile(@PathVariable Long id, @RequestBody KYCFileOutputDto kycFileOutputDto){
        KYCFileOutputDto updateKYCFile = this.kycFileService.updateKYCFile(id, kycFileOutputDto);
        return ResponseEntity.ok(updateKYCFile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteKYCFileById(@PathVariable long id){
        String result = kycFileService.deleteKYCFile(id);
        return ResponseEntity.ok(result);

    }




}
