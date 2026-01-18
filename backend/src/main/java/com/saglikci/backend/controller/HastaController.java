package com.saglikci.backend.controller;

import com.saglikci.backend.model.Hasta;
import com.saglikci.backend.model.Personel;
import com.saglikci.backend.model.Randevu;
import com.saglikci.backend.model.HastaIlac;
import com.saglikci.backend.model.Ilac;

import com.saglikci.backend.repository.HastaRepository;
import com.saglikci.backend.repository.HastaIlacRepository;
import com.saglikci.backend.repository.RandevuRepository;
import com.saglikci.backend.repository.IlacRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class HastaController {

    @Autowired
    private HastaRepository hastaRepository;

    @Autowired
    private HastaIlacRepository hastaIlacRepository;

    @Autowired
    private RandevuRepository randevuRepository;

    @Autowired 
    private IlacRepository ilacRepository;


    // --- 1. BEDENSEL HASTA İŞLEMLERİ ---

    @GetMapping("/bedensel/ekle")
    public String bedenselEkleSayfasi(HttpSession session, Model model) {
        if (session.getAttribute("girisYapanKullanici") == null) { return "redirect:/giris"; }
        
        model.addAttribute("baslik", "Bedensel Hasta Ekle");
        model.addAttribute("kategoriId", 2);
        model.addAttribute("hasta", new Hasta()); 
        return "hasta-ekle"; 
    }

    @PostMapping("/bedensel/kaydet")
    public String bedenselKaydet(@ModelAttribute Hasta hasta, HttpSession session, RedirectAttributes redirectAttributes) {
        Personel personel = (Personel) session.getAttribute("girisYapanKullanici");
        if (personel == null) { return "redirect:/giris"; }

        hasta.setPersonelId(personel.getId());
        hasta.setKategoriId(2); 
        
        hastaRepository.save(hasta);
        
        // hasta kaydı başarılı mesajı
        redirectAttributes.addFlashAttribute("mesaj", "Bedensel hasta kaydı başarıyla oluşturuldu!");
        redirectAttributes.addFlashAttribute("mesajTuru", "basarili");

        return "redirect:/panel"; 
    }

    // --- 2. RUHSAL HASTA İŞLEMLERİ ---

    @GetMapping("/ruhsal/ekle")
    public String ruhsalEkleSayfasi(HttpSession session, Model model) {
        if (session.getAttribute("girisYapanKullanici") == null) { return "redirect:/giris"; }

        model.addAttribute("baslik", "Ruhsal Hasta Ekle");
        model.addAttribute("kategoriId", 1); 
        model.addAttribute("hasta", new Hasta()); 
        return "hasta-ekle";
    }

    @PostMapping("/ruhsal/kaydet")
    public String ruhsalKaydet(@ModelAttribute Hasta hasta, HttpSession session, RedirectAttributes redirectAttributes) {
        Personel personel = (Personel) session.getAttribute("girisYapanKullanici");
        if (personel == null) { return "redirect:/giris"; }

        hasta.setPersonelId(personel.getId());
        hasta.setKategoriId(1);
        
        hastaRepository.save(hasta);
        
        redirectAttributes.addFlashAttribute("mesaj", "Ruhsal hasta kaydı başarıyla oluşturuldu!");
        redirectAttributes.addFlashAttribute("mesajTuru", "basarili");

        return "redirect:/panel";
    }

    // --- GENEL HASTA İŞLEMLERİ (SİLME & DÜZENLEME & DETAY) ---

    // 1. HASTA SİLME
   @GetMapping("/hasta/sil/{id}")
    public String hastaSil(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("girisYapanKullanici") == null) { return "redirect:/giris"; }

        Hasta hasta = hastaRepository.findById(id).orElse(null);
        if (hasta != null) {
            int kategori = hasta.getKategoriId();
            hastaRepository.delete(hasta);
            
            // Silince de mesaj verelim
            redirectAttributes.addFlashAttribute("mesaj", "Hasta kaydı ve verileri silindi.");
            redirectAttributes.addFlashAttribute("mesajTuru", "hata"); 
            
            return kategori == 1 ? "redirect:/ruhsal" : "redirect:/bedensel";
        }
        return "redirect:/panel";
    }
    // 2. HASTA DÜZENLEME
    @GetMapping("/hasta/duzenle/{id}")
    public String hastaDuzenle(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("girisYapanKullanici") == null) { return "redirect:/giris"; }

        Hasta hasta = hastaRepository.findById(id).orElse(null);
        if (hasta == null) return "redirect:/panel";

        model.addAttribute("baslik", "Hasta Bilgilerini Düzenle");
        model.addAttribute("kategoriId", hasta.getKategoriId());
        model.addAttribute("hasta", hasta); 

        return "hasta-ekle"; 
    }

    // 3. HASTA DETAY SAYFASI
    @GetMapping("/hasta/detay/{id}")
    public String hastaDetay(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("girisYapanKullanici") == null) { return "redirect:/giris"; }

        Hasta hasta = hastaRepository.findById(id).orElse(null);
        if (hasta == null) { return "redirect:/panel"; }

        var ilaclar = hastaIlacRepository.findByHastaId(id);
        var randevular = randevuRepository.findByHastaId(id);

        model.addAttribute("hasta", hasta);
        model.addAttribute("ilaclar", ilaclar);
        model.addAttribute("randevular", randevular);

        return "hasta-detay"; 
    }


    // --- İLAÇ İŞLEMLERİ ---

    // 1. İlaç Ekleme Sayfası
    @GetMapping("/hasta/ilac-ekle/{hastaId}")
    public String ilacEkleSayfasi(@PathVariable Long hastaId, HttpSession session, Model model) {
        if (session.getAttribute("girisYapanKullanici") == null) { return "redirect:/giris"; }

        Hasta hasta = hastaRepository.findById(hastaId).orElse(null);
        if (hasta == null) return "redirect:/panel";

        model.addAttribute("hasta", hasta);
        return "hasta-ilac-ekle"; 
    }

    // 2. İlacı Kaydet 
    @PostMapping("/hasta/ilac-kaydet")
    public String ilacKaydet(@RequestParam Long hastaId,
                             @RequestParam String ilacAdi,
                             @RequestParam String dozaj,
                             @RequestParam String kullanimSikligi, 
                             @RequestParam LocalDate baslangicTarihi,
                             @RequestParam String bitisTarihi) {
        
        // --- A. İLAÇ KONTROLÜ ---
        Ilac ilac = ilacRepository.findByAd(ilacAdi);

        if (ilac == null) {
            ilac = new Ilac();
            ilac.setAd(ilacAdi);
            ilac.setAciklama("Otomatik eklendi");
            ilacRepository.save(ilac);
        }

        // --- B. HASTAYA İLAÇ ATAMA ---
        HastaIlac yeniKayit = new HastaIlac();

        yeniKayit.setHastaId(hastaId);

        yeniKayit.setIlacId(ilac.getId());

        yeniKayit.setDozaj(dozaj);
        
        yeniKayit.setKullanimSikligi(kullanimSikligi);

        yeniKayit.setBaslangicTarihi(baslangicTarihi);
        
        yeniKayit.setBitisTarihi(LocalDate.parse(bitisTarihi));

        hastaIlacRepository.save(yeniKayit);

        return "redirect:/hasta/detay/" + hastaId;
    }

    // 3. İlaç Silme
    @GetMapping("/hasta/ilac-sil/{id}")
    public String hastaIlacSil(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("girisYapanKullanici") == null) { return "redirect:/giris"; }

        HastaIlac kayit = hastaIlacRepository.findById(id).orElse(null);
        
        if (kayit != null) {
            Long hastaId = kayit.getHastaId(); 
            hastaIlacRepository.delete(kayit); 
            return "redirect:/hasta/detay/" + hastaId; 
        }
        return "redirect:/panel";
    }


    // --- RANDEVU İŞLEMLERİ ---

    // 1. Randevu Ekleme Sayfası
    @GetMapping("/hasta/randevu-ekle/{hastaId}")
    public String randevuEkleSayfasi(@PathVariable Long hastaId, HttpSession session, Model model) {
        if (session.getAttribute("girisYapanKullanici") == null) { return "redirect:/giris"; }

        Hasta hasta = hastaRepository.findById(hastaId).orElse(null);
        if (hasta == null) return "redirect:/panel";

        model.addAttribute("hasta", hasta);
        return "hasta-randevu-ekle"; 
    }

    // 2. Randevuyu Kaydet
    @PostMapping("/hasta/randevu-kaydet")
    public String randevuKaydet(@RequestParam Long hastaId,
                                @RequestParam String randevuTarihi, 
                                @RequestParam String randevuAciklamasi,
                                HttpSession session) {
        
        Personel personel = (Personel) session.getAttribute("girisYapanKullanici");
        if (personel == null) { return "redirect:/giris"; }

        Randevu randevu = new Randevu();
        randevu.setHastaId(hastaId);
        randevu.setPersonelId(personel.getId());
        randevu.setAciklama(randevuAciklamasi);
        randevu.setDurum("Beklemede");
        
        randevu.setRandevuTarihi(java.time.LocalDateTime.parse(randevuTarihi));

        randevuRepository.save(randevu);

        return "redirect:/hasta/detay/" + hastaId;
    }

    // 3. Randevu Silme
    @GetMapping("/hasta/randevu-sil/{id}")
    public String randevuSil(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("girisYapanKullanici") == null) { return "redirect:/giris"; }

        Randevu randevu = randevuRepository.findById(id).orElse(null);
        
        if (randevu != null) {
            Long hastaId = randevu.getHastaId();
            randevuRepository.delete(randevu);
            return "redirect:/hasta/detay/" + hastaId;
        }
        return "redirect:/panel";
    }
}