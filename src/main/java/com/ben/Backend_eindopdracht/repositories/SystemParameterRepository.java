package com.ben.Backend_eindopdracht.repositories;


import com.ben.Backend_eindopdracht.models.SystemParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemParameterRepository extends JpaRepository<SystemParameter, Long> {
}
