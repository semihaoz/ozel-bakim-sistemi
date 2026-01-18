package com.saglikci.backend.repository;

import com.saglikci.backend.model.Hasta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HastaRepository extends JpaRepository<Hasta, Long> {
    
    List<Hasta> findByKategoriId(Integer kategoriId);
    List<Hasta> findByAdSoyadContainingIgnoreCaseAndKategoriId(String adSoyad, Integer kategoriId);
    long countByKategoriId(Integer kategoriId);

    
    // 1. Personelin belli kategorideki hastalarını getir
    List<Hasta> findByKategoriIdAndPersonelId(Integer kategoriId, Integer personelId);

    // 2. Personelin kendi hastaları içinde arama yapması
    List<Hasta> findByAdSoyadContainingIgnoreCaseAndKategoriIdAndPersonelId(String adSoyad, Integer kategoriId, Integer personelId);

    // 3. Personelin kendi hasta sayısını sayması
    long countByKategoriIdAndPersonelId(Integer kategoriId, Integer personelId);
}