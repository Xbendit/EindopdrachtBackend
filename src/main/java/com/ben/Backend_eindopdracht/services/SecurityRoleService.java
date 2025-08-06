package com.ben.Backend_eindopdracht.services;

import com.ben.Backend_eindopdracht.dtos.SecurityRoleOutputDto;
import com.ben.Backend_eindopdracht.exceptions.RecordNotFoundException;
import com.ben.Backend_eindopdracht.mappers.SecurityRoleMapper;
import com.ben.Backend_eindopdracht.models.SecurityRole;
import com.ben.Backend_eindopdracht.repositories.SecurityRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class SecurityRoleService {

    private final SecurityRoleRepository securityRoleRepository;

    public SecurityRoleService(SecurityRoleRepository securityRoleRepository) {
        this.securityRoleRepository = securityRoleRepository;
    }

    public SecurityRole save(SecurityRole securityRole) {
        return securityRoleRepository.save(securityRole);
    }

    public SecurityRoleOutputDto updateSecurityRole(long id, SecurityRoleOutputDto securityRoleOutputDto){
        SecurityRole securityRole = securityRoleRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("SecurityRole " + id + " not found"));

        securityRole.setRole(securityRoleOutputDto.getRole());

        SecurityRole savedSecurityRole = securityRoleRepository.save(securityRole);

        return SecurityRoleMapper.toOutputDto(savedSecurityRole);
    }
}
