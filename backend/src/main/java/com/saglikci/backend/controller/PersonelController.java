package com.saglikci.backend.controller;

import com.saglikci.backend.model.Personel;
import com.saglikci.backend.repository.PersonelRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PersonelController {

    private final PersonelRepository personelRepository;

    public PersonelController(PersonelRepository personelRepository) {
        this.personelRepository = personelRepository;
    }

    @GetMapping("/api/personel")
    public List<Personel> getAllPersonel() {
        return personelRepository.findAll();
    }
}
