package com.example.tingeso.controllers;

import com.example.tingeso.services.JustificativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping
public class JustificativoController {

    @Autowired
    private JustificativoService justificativoService;

    @GetMapping("/subirJustificativo")
    public String nuevoJustificativo(){
        return "subirJustificativo";
    }
    @PostMapping("/guardarJustificativo")
    public String guardarJustificativo(@RequestParam Map<String,String> allParams) {
        justificativoService.guardarJustificativo(allParams);
        return "redirect:/subirJustificativo";
    }
}
