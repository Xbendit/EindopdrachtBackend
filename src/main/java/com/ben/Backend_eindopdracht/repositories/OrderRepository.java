package com.ben.Backend_eindopdracht.repositories;

import com.ben.Backend_eindopdracht.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long>{
}
