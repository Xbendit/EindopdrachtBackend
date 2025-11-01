package com.ben.Backend_eindopdracht.mappers;

import com.ben.Backend_eindopdracht.dtos.KYCFileInputDto;
import com.ben.Backend_eindopdracht.dtos.KYCFileOutputDto;
import com.ben.Backend_eindopdracht.models.KYCFile;

public class KYCFileMapper {
    public static KYCFile toEntity(KYCFileInputDto dto) {
        KYCFile k = new KYCFile();
        k.setFileName(dto.getFileName());
        k.setFilePath(dto.getFilePath());
        k.setFileStatus(dto.getFileStatus());
        return k;

    }

    public static KYCFileOutputDto toOutputDto(KYCFile f) {
        KYCFileOutputDto dto = new KYCFileOutputDto();
        dto.setId(f.getId());
        dto.setFileName(f.getFileName());
        dto.setFilePath(f.getFilePath());
        dto.setFileSize(f.getFileSize());
        dto.setFileStatus(f.getFileStatus());
        dto.setUserId(f.getUsers() != null ? f.getUsers().getId() : null);
        return dto;
    }

}
