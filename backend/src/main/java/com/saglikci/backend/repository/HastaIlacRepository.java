package com.saglikci.backend.repository;

import com.saglikci.backend.model.HastaIlac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface HastaIlacRepository extends JpaRepository<HastaIlac, Long> {

    // Kategoriye ve Tarihe Göre İlaç Sayısı (SQL JOIN)
    @Query(value = "SELECT COUNT(*) FROM hasta_ilac hi " +
                   "JOIN hasta h ON hi.hasta_id = h.id " +
                   "WHERE hi.bitis_tarihi <= :tarih AND h.kategori_id = :kategoriId", 
           nativeQuery = true)
    long countIlacByKategori(@Param("tarih") LocalDate tarih, @Param("kategoriId") Long kategoriId);

    List<HastaIlac> findByHastaId(Long hastaId);

    // --(Kişiye Özel İlaç Sayısı) ---
    @Query("SELECT COUNT(hi) FROM HastaIlac hi WHERE hi.bitisTarihi > :tarih AND hi.hastaId IN (SELECT h.id FROM Hasta h WHERE h.kategoriId = :kategoriId AND h.personelId = :personelId)")
    long countByTarihAndKategoriIdAndPersonelId(LocalDate tarih, Integer kategoriId, Integer personelId);

    // --- AKILLI ASİSTAN SORGUSU: KRİTİK İLAÇLAR (3 gün) ---
    @Query("SELECT COUNT(hi) FROM HastaIlac hi WHERE " +
           "hi.bitisTarihi BETWEEN :baslangic AND :bitis " +
           "AND hi.hastaId IN (SELECT h.id FROM Hasta h WHERE h.kategoriId = :kategoriId AND h.personelId = :personelId)")
    long countKritikIlac(LocalDate baslangic, LocalDate bitis, Integer kategoriId, Integer personelId);
}