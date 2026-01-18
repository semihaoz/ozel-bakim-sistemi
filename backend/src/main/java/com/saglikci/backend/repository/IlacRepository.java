package com.saglikci.backend.repository;

import com.saglikci.backend.model.Ilac;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IlacRepository extends JpaRepository<Ilac, Long> {
    
    Ilac findByAd(String ad);
}