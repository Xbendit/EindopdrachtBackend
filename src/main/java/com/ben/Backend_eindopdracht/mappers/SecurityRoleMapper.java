package com.ben.Backend_eindopdracht.mappers;

import com.ben.Backend_eindopdracht.dtos.SecurityRoleInputDto;
import com.ben.Backend_eindopdracht.dtos.SecurityRoleOutputDto;
import com.ben.Backend_eindopdracht.models.SecurityRole;

public class SecurityRoleMapper {

// van entity naar OutputDTO
    public static SecurityRoleOutputDto toOutputDto(SecurityRole securityRole) {
        var r = new SecurityRoleOutputDto();
        r.setId(securityRole.getId());
        r.setRole(securityRole.getRole());
        return r;
    }


    // van SecurityRoleInputDTO naar Entity
    public static SecurityRole toEntity(SecurityRoleInputDto dto) {
        SecurityRole s = new SecurityRole();
        s.setRole(dto.getRole());
        return s;
    }


}
