package com.saglikci.backend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hasta_ilac")
public class HastaIlac {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hasta_id")
    private Long hastaId;

    @Column(name = "ilac_id")
    private Long ilacId;

    @ManyToOne
    @JoinColumn(name = "ilac_id", insertable = false, updatable = false)
    private Ilac ilac;

    @Column(name = "kullanim_sikligi")
    private String kullanimSikligi;

    private String dozaj;
    private LocalDate bitisTarihi;

    private LocalDate baslangicTarihi; 

    // --- GETTER VE SETTER'LAR ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHastaId() { return hastaId; }
    public void setHastaId(Long hastaId) { this.hastaId = hastaId; }

    public Long getIlacId() { return ilacId; }
    public void setIlacId(Long ilacId) { this.ilacId = ilacId; }

    public Ilac getIlac() { return ilac; }
    public void setIlac(Ilac ilac) { this.ilac = ilac; }

    public String getKullanimSikligi() { return kullanimSikligi; }
    public void setKullanimSikligi(String kullanimSikligi) { this.kullanimSikligi = kullanimSikligi; }

    public String getDozaj() { return dozaj; }
    public void setDozaj(String dozaj) { this.dozaj = dozaj; }

    public LocalDate getBitisTarihi() { return bitisTarihi; }
    public void setBitisTarihi(LocalDate bitisTarihi) { this.bitisTarihi = bitisTarihi; }

    public LocalDate getBaslangicTarihi() {return baslangicTarihi;}

    public void setBaslangicTarihi(LocalDate baslangicTarihi) {this.baslangicTarihi = baslangicTarihi;}
}