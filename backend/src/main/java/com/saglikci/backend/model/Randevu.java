package com.saglikci.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "randevu")
public class Randevu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "randevu_tarihi")
    private LocalDateTime randevuTarihi;

    private String aciklama;
    private String durum; // "Beklemede", "Tamamlandı" vb.

    @Column(name = "hasta_id")
    private Long hastaId; 

    @Column(name = "personel_id")
    private Integer personelId; 

    // --- GETTER VE SETTER METOTLARI ---
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getRandevuTarihi() { return randevuTarihi; }
    public void setRandevuTarihi(LocalDateTime randevuTarihi) { this.randevuTarihi = randevuTarihi; }

    public String getAciklama() { return aciklama; }
    public void setAciklama(String aciklama) { this.aciklama = aciklama; }

    public String getDurum() { return durum; }
    public void setDurum(String durum) { this.durum = durum; }

    public Long getHastaId() { return hastaId; }
    public void setHastaId(Long hastaId) { this.hastaId = hastaId; }

    public Integer getPersonelId() { return personelId; }
    public void setPersonelId(Integer personelId) { this.personelId = personelId; }
}