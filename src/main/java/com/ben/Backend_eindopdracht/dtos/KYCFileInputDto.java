package com.ben.Backend_eindopdracht.dtos;

import com.ben.Backend_eindopdracht.models.KYCFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KYCFileInputDto {

    private String fileName;
    private String filePath;
    /*private String fileStatus;*/
    private KYCFile.KycFileStatus fileStatus;
}
