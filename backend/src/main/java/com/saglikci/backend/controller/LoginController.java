package com.saglikci.backend.controller;

import com.saglikci.backend.model.Personel;
import com.saglikci.backend.repository.PersonelRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private PersonelRepository personelRepository;

    // 1. Giriş Sayfasını Göster (GET)
    @GetMapping("/giris")
    public String girisSayfasi() {
        return "giris"; // templates/giris.html dosyasını açar
    }

    // 2. Giriş Yap Butonuna Basılınca Çalışır (POST)
    @PostMapping("/giris-yap")
    public String girisKontrol(@RequestParam String kullaniciAdi,
                               @RequestParam String sifre,
                               HttpSession session) {

        // Veritabanında bu kullanıcı adına sahip kişiyi bul
        Personel personel = personelRepository.findByKullaniciAdi(kullaniciAdi);

        // Kullanıcı var mı ve şifresi doğru mu?
        if (personel != null && personel.getSifre().equals(sifre)) {
            // BAŞARILI: Oturumu başlat ve panele gönder
            session.setAttribute("girisYapanKullanici", personel);
            return "redirect:/panel";
        } else {
            // HATALI: Tekrar giriş sayfasına gönder (Hata mesajı için ?error ekledik)
            return "redirect:/giris?error";
        }
    }

    // 3. Çıkış Yap Butonuna Basılınca (GET)
    @GetMapping("/cikis")
    public String cikisYap(HttpSession session) {
        session.invalidate(); // Oturumu öldür
        return "redirect:/giris";
    }
}