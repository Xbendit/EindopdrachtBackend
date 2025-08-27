package com.ben.Backend_eindopdracht.repositories;


import com.ben.Backend_eindopdracht.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long>{
    Optional<User> findByUsername(String username);
}
