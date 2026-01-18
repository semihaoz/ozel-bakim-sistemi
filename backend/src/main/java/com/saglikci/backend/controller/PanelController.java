package com.saglikci.backend.controller;

import com.saglikci.backend.model.Hasta;
import com.saglikci.backend.model.Personel;
import com.saglikci.backend.repository.HastaIlacRepository;
import com.saglikci.backend.repository.RandevuRepository;
import com.saglikci.backend.repository.HastaRepository; 

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PanelController {

    @Autowired private RandevuRepository randevuRepository;
    @Autowired private HastaIlacRepository hastaIlacRepository;
    @Autowired private HastaRepository hastaRepository;

    // --- ANA PANEL (DASHBOARD) ---
    @GetMapping("/panel")
    public String panelGoster(HttpSession session, Model model) {
        Personel personel = (Personel) session.getAttribute("girisYapanKullanici");
        if (personel == null) { return "redirect:/giris"; }
        
        model.addAttribute("kullanici", personel);

        // --- TARİH HESAPLAMALARI ---
        LocalDate bugunDate = LocalDate.now();
        LocalDate ucGunSonraDate = bugunDate.plusDays(3); // İlaçlar için (LocalDate)

        LocalDateTime simdiTime = LocalDateTime.now();
        LocalDateTime ucGunSonraTime = simdiTime.plusDays(3); // Randevular için (LocalDateTime)

        // --- 1. BEDENSEL VERİLERİ ---
        // A) İlaçlar (Sadece Kritikleri alıyoruz)
        long bedenselKritikIlac = hastaIlacRepository.countKritikIlac(bugunDate, ucGunSonraDate, 2, personel.getId());
        
        // B) Randevular (Hem Toplam Bekleyen hem de Yaklaşan Acil olanlar)
        long bedenselToplamRandevu = randevuRepository.countByDurumAndKategoriIdAndPersonelId("Beklemede", 2, personel.getId());
        long bedenselKritikRandevu = randevuRepository.countYaklasanRandevu(simdiTime, ucGunSonraTime, 2, personel.getId());

        model.addAttribute("bedenselKritikIlac", bedenselKritikIlac);
        model.addAttribute("bedenselToplamRandevu", bedenselToplamRandevu);
        model.addAttribute("bedenselKritikRandevu", bedenselKritikRandevu);


        // --- 2. RUHSAL VERİLERİ ---
        long ruhsalKritikIlac = hastaIlacRepository.countKritikIlac(bugunDate, ucGunSonraDate, 1, personel.getId());
        
        long ruhsalToplamRandevu = randevuRepository.countByDurumAndKategoriIdAndPersonelId("Beklemede", 1, personel.getId());
        long ruhsalKritikRandevu = randevuRepository.countYaklasanRandevu(simdiTime, ucGunSonraTime, 1, personel.getId());
        
        model.addAttribute("ruhsalKritikIlac", ruhsalKritikIlac);
        model.addAttribute("ruhsalToplamRandevu", ruhsalToplamRandevu);
        model.addAttribute("ruhsalKritikRandevu", ruhsalKritikRandevu);

        return "panel";
    }

    // --- BEDENSEL SAYFASI (Güvenli & Kişiye Özel) ---
    @GetMapping("/bedensel")
    public String bedenselSayfa(HttpSession session, Model model, @RequestParam(required = false) String arama) {
        Personel personel = (Personel) session.getAttribute("girisYapanKullanici");
        if (personel == null) { return "redirect:/giris"; }
        
        List<Hasta> hastaListesi;

        // Arama kutusu dolu mu?
        if (arama != null && !arama.isEmpty()) {
            // GÜVENLİK GÜNCELLEMESİ: Sadece BU personelin hastaları içinde ara
            hastaListesi = hastaRepository.findByAdSoyadContainingIgnoreCaseAndKategoriIdAndPersonelId(arama, 2, personel.getId());
        } else {
            // GÜVENLİK GÜNCELLEMESİ: Sadece BU personelin tüm bedensel hastalarını getir
            hastaListesi = hastaRepository.findByKategoriIdAndPersonelId(2, personel.getId());
        }

       // Sadece BUGÜN ve YARIN olan randevularda ünlem çıksın
        LocalDateTime simdi = LocalDateTime.now();
        LocalDateTime yarinGece = simdi.plusDays(1).withHour(23).withMinute(59); // Yarın gün bitimine kadar

        for (Hasta h : hastaListesi) {
            boolean acilDurum = randevuRepository.existsByHastaIdAndDurumAndRandevuTarihiBetween(
                h.getId(), "Beklemede", simdi, yarinGece
            );
            h.setRandevusuVarMi(acilDurum);
        }
        
        model.addAttribute("baslik", "Bedensel Sağlık Hastaları");
        model.addAttribute("hastalar", hastaListesi);
        return "hasta-listele";
    }

    // --- RUHSAL SAYFASI (Güvenli & Kişiye Özel) ---
    @GetMapping("/ruhsal")
    public String ruhsalSayfa(HttpSession session, Model model, @RequestParam(required = false) String arama) {
        Personel personel = (Personel) session.getAttribute("girisYapanKullanici");
        if (personel == null) { return "redirect:/giris"; }
        
        List<Hasta> hastaListesi;

        // Arama kutusu dolu mu?
        if (arama != null && !arama.isEmpty()) {
            // GÜVENLİK GÜNCELLEMESİ: Sadece BU personelin hastaları içinde ara
            hastaListesi = hastaRepository.findByAdSoyadContainingIgnoreCaseAndKategoriIdAndPersonelId(arama, 1, personel.getId());
        } else {
            // GÜVENLİK GÜNCELLEMESİ: Sadece BU personelin tüm ruhsal hastalarını getir
            hastaListesi = hastaRepository.findByKategoriIdAndPersonelId(1, personel.getId());
        }

        // Randevu Kontrolü
        LocalDateTime simdi = LocalDateTime.now();
        LocalDateTime yarinGece = simdi.plusDays(1).withHour(23).withMinute(59);

        for (Hasta h : hastaListesi) {
            boolean acilDurum = randevuRepository.existsByHastaIdAndDurumAndRandevuTarihiBetween(
                h.getId(), "Beklemede", simdi, yarinGece
            );
            h.setRandevusuVarMi(acilDurum);
        }
        
        model.addAttribute("baslik", "Ruhsal Sağlık Hastaları");
        model.addAttribute("hastalar", hastaListesi);
        return "hasta-listele";
    }
}