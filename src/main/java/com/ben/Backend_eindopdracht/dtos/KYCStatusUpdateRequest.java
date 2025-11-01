package com.ben.Backend_eindopdracht.dtos;

import com.ben.Backend_eindopdracht.models.KYCFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KYCStatusUpdateRequest {
    private KYCFile.KycFileStatus status;
    public KYCFile.KycFileStatus getStatus() { return status; }
    public void setStatus(KYCFile.KycFileStatus status) { this.status = status; }
}
