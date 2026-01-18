package com.saglikci.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hasta")
public class Hasta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(name = "ad_soyad")
    private String adSoyad;

    private Integer yas;
    private String cinsiyet;
    private String hastalik;

   
    @Column(name = "kategori_id")
    private Integer kategoriId; 

 
    @Column(name = "personel_id")
    private Integer personelId; 

    @Column(name = "vasi_ad_soyad")
    private String vasiAdSoyad;

    @Column(name = "vasi_telefon")
    private String vasiTelefon;

    @Transient
    private boolean randevusuVarMi;

    // --- GETTER VE SETTER METOTLARI ---
    public boolean isRandevusuVarMi() { return randevusuVarMi; }
    public void setRandevusuVarMi(boolean randevusuVarMi) { this.randevusuVarMi = randevusuVarMi; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAdSoyad() { return adSoyad; }
    public void setAdSoyad(String adSoyad) { this.adSoyad = adSoyad; }

    public Integer getYas() { return yas; }
    public void setYas(Integer yas) { this.yas = yas; }

    public String getCinsiyet() { return cinsiyet; }
    public void setCinsiyet(String cinsiyet) { this.cinsiyet = cinsiyet; }

    public String getHastalik() { return hastalik; }
    public void setHastalik(String hastalik) { this.hastalik = hastalik; }

    public Integer getKategoriId() { return kategoriId; }
    public void setKategoriId(Integer kategoriId) { this.kategoriId = kategoriId; }

    public Integer getPersonelId() { return personelId; }
    public void setPersonelId(Integer personelId) { this.personelId = personelId; }

    public String getVasiAdSoyad() { return vasiAdSoyad; }
    public void setVasiAdSoyad(String vasiAdSoyad) { this.vasiAdSoyad = vasiAdSoyad; }

    public String getVasiTelefon() { return vasiTelefon; }
    public void setVasiTelefon(String vasiTelefon) { this.vasiTelefon = vasiTelefon; }
}