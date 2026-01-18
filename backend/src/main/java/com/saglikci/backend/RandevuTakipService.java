package com.saglikci.backend;

import com.saglikci.backend.model.Randevu;
import com.saglikci.backend.repository.RandevuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component; 

import java.time.LocalDateTime;
import java.util.List;

@Component
public class RandevuTakipService {

    @Autowired
    private RandevuRepository randevuRepository;

    // Bu metod her 60.000 milisaniyede (1 dakikada) bir çalışır
    @Scheduled(fixedRate = 60000)
    public void randevuDurumlariniGuncelle() {
        
        // 1. Şu anki zamanı al
        LocalDateTime suAn = LocalDateTime.now();

        // 2. Tarihi geçmiş ve hala 'Beklemede' olanları bul
        List<Randevu> gecmisRandevular = randevuRepository.findByDurumAndRandevuTarihiBefore("Beklemede", suAn);

        // 3. Eğer varsa hepsini güncelle
        if (!gecmisRandevular.isEmpty()) {
            for (Randevu r : gecmisRandevular) {
                r.setDurum("Tamamlandı");
            }
            // Hepsini tek seferde kaydet
            randevuRepository.saveAll(gecmisRandevular);
            
            System.out.println("🤖 ROBOT: " + gecmisRandevular.size() + " adet randevunun durumu güncellendi.");
        }
    }
}