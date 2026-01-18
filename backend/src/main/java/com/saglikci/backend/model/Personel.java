package com.saglikci.backend.model;
import jakarta.persistence.*;

@Entity
@Table(name = "personel")
public class Personel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ad_soyad", nullable = false, length = 20)
    private String adSoyad;

    @Column(name = "kullanici_adi", nullable = false, unique = true, length = 10)
    private String kullaniciAdi;

    @Column(name = "sifre", nullable = false, length = 20)
    private String sifre;

    @Column(name = "telefon", length = 11)
    private String telefon;

    // Boş constructor (JPA için zorunlu)
    public Personel() {
    }

    // İsteğe bağlı: full constructor
    public Personel(String adSoyad, String kullaniciAdi, String sifre, String telefon) {
        this.adSoyad = adSoyad;
        this.kullaniciAdi = kullaniciAdi;
        this.sifre = sifre;
        this.telefon = telefon;
    }

    // GETTER - SETTER'lar
    public Integer getId() {
        return id;
    }

    public String getAdSoyad() {
        return adSoyad;
    }

    public void setAdSoyad(String adSoyad) {
        this.adSoyad = adSoyad;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
}