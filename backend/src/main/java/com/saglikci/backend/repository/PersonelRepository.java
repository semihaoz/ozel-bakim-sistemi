package com.saglikci.backend.repository;

import com.saglikci.backend.model.Personel;
import org.springframework.data.jpa.repository.JpaRepository;

// JpaRepository<ModelAdı, IDTürü>
public interface PersonelRepository extends JpaRepository<Personel, Long> {
    
    // Kullanıcı adına göre personel bul
   Personel findByKullaniciAdi(String kullaniciAdi);
}