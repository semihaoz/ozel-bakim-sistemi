package com.saglikci.backend.controller;

import com.saglikci.backend.model.Personel;
import com.saglikci.backend.repository.PersonelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class KayitController {

    @Autowired
    private PersonelRepository personelRepository;

    // 1. Kayıt Sayfasını Göster (GET)
    @GetMapping("/kayit")
    public String kayitSayfasiGoster(Model model) {
        // HTML formunun (th:object="${personel}") çalışabilmesi için
        // sayfaya boş bir Personel nesnesi gönderiyoruz.
        model.addAttribute("personel", new Personel());
        
        return "kayit"; 
    }

    // 2. Kayıt Ol Butonuna Basılınca (POST)
    @PostMapping("/kayit")
    public String kayitOl(@ModelAttribute Personel personel, RedirectAttributes redirectAttributes) {
        
        // Veriyi kaydet
        personelRepository.save(personel);
        
        // Girişin başarılı olduğunu mesajla bildirir
        redirectAttributes.addFlashAttribute("mesaj", "Kayıt işlemi başarılı! Lütfen giriş yapınız.");
        redirectAttributes.addFlashAttribute("mesajTuru", "basarili");

        // Giriş sayfasına yönlendir
        return "redirect:/giris"; 
    }
}