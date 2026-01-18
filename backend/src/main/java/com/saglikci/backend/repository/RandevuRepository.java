package com.saglikci.backend.repository;

import com.saglikci.backend.model.Randevu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List; 

public interface RandevuRepository extends JpaRepository<Randevu, Long> {

    // Kategoriye ve Duruma Göre Randevu Sayısı (SQL JOIN)
    @Query(value = "SELECT COUNT(*) FROM randevu r " +
                   "JOIN hasta h ON r.hasta_id = h.id " +
                   "WHERE r.durum = :durum AND h.kategori_id = :kategoriId", 
           nativeQuery = true)
    long countRandevuByKategori(@Param("durum") String durum, @Param("kategoriId") Long kategoriId);

    List<Randevu> findByHastaId(Long hastaId);

    List<Randevu> findByDurumAndRandevuTarihiBefore(String durum, LocalDateTime tarih);

    // Hastanın BEKLEMEDE olan bir randevusu var mı kontrol et (Varsa true döner)
    boolean existsByHastaIdAndDurum(Long hastaId, String durum);

    // Randevunun kendisinde kategori yok, o yüzden Hasta tablosuna gidip bakıyoruz (Subquery)
    @Query("SELECT COUNT(r) FROM Randevu r WHERE r.durum = :durum AND r.hastaId IN (SELECT h.id FROM Hasta h WHERE h.kategoriId = :kategoriId AND h.personelId = :personelId)")
    long countByDurumAndKategoriIdAndPersonelId(String durum, Integer kategoriId, Integer personelId);

// --- AKILLI ASİSTAN: YAKLAŞAN RANDEVULAR ---
    @Query("SELECT COUNT(r) FROM Randevu r WHERE r.durum = 'Beklemede' " +
           "AND r.randevuTarihi BETWEEN :baslangic AND :bitis " +
           "AND r.hastaId IN (SELECT h.id FROM Hasta h WHERE h.kategoriId = :kategoriId AND h.personelId = :personelId)")
    long countYaklasanRandevu(LocalDateTime baslangic, LocalDateTime bitis, Integer kategoriId, Integer personelId);

    // Sadece belirli tarih aralığındaki (yaklaşan) bekleyen randevuları kontrol eder
    boolean existsByHastaIdAndDurumAndRandevuTarihiBetween(Long hastaId, String durum, LocalDateTime baslangic, LocalDateTime bitis);
    
}